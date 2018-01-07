package si.betoo.hodler.data.coin

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import si.betoo.hodler.data.database.Database
import si.betoo.hodler.data.api.CryptoCompareAPI
import si.betoo.hodler.data.cryptocompare.CryptoCompareCoinList
import si.betoo.hodler.data.cryptocompare.CryptoCompareWrapper
import timber.log.Timber
import java.util.ArrayList

class CoinService(private val provideCryptoCompareAPI: CryptoCompareAPI, private var database: Database) {

    private val coinsFromAPI = ArrayList<Coin>()
    private val observableAvailableCoins = PublishSubject.create<List<Coin>>()

    private val cachedPrices: MutableMap<String, CachePriceWrapper> = HashMap()

    companion object {
        const val PRICE_CACHE_IN_MS = 15000
    }

    fun getActiveCoins(): Observable<List<Coin>> {
        return database.coinDAO()
                .getActiveCoins()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .share()
    }

    fun getAvailableCoins(): Observable<List<Coin>> {
        getActiveCoins().subscribe { activeCoins ->
            if (coinsFromAPI.size > 0) {
                observableAvailableCoins.onNext(mergeAvailableWithActive(activeCoins, coinsFromAPI))
            } else {
                provideCryptoCompareAPI
                        .getCoinList()
                        .subscribeOn(Schedulers.io())
                        .subscribe({ input ->
                            val coins = transformToCoins(input)

                            coinsFromAPI.clear()
                            coinsFromAPI.addAll(coins)

                            observableAvailableCoins.onNext(mergeAvailableWithActive(activeCoins, coinsFromAPI))
                        }, { error -> Timber.e(error) })
            }
        }

        return observableAvailableCoins
    }

    fun saveCoin(item: Coin) {
        Observable.just(database)
                .subscribeOn(Schedulers.io())
                .subscribe { db -> db.coinDAO().insert(item) }
    }


    fun getPricesForCoins(symbols: String, currencies: String): Observable<List<Price>> {
        val cachedPrice = cachedPrices[symbols]

        if (cachedPrice != null && cachedPrice.timestamp > (System.currentTimeMillis() - PRICE_CACHE_IN_MS)) {
            return Observable.just(cachedPrice.prices)
        }

        return provideCryptoCompareAPI.getCoinPriceMultiFull(symbols, currencies)
                .map { prices ->
                    val results = ArrayList<Price>()

                    for (raw in prices.raw) {

                        for (currency in raw.value.data) {
                            results.add(Price(raw.key, currency.key, currency.value.PRICE, currency.value.CHANGEPCT24HOUR, currency.value.LASTUPDATE))
                        }
                    }

                    cachedPrices.put(symbols, CachePriceWrapper(System.currentTimeMillis(), results))

                    results
                }
    }

    private fun mergeAvailableWithActive(activeCoins: List<Coin>, coinsFromAPI: List<Coin>): List<Coin> {
        val results = ArrayList<Coin>()

        for (coin in coinsFromAPI) {
            val copy = coin.copy()

            for (activeCoin in activeCoins) {
                if (copy.symbol == activeCoin.symbol) {
                    copy.isActive = activeCoin.isActive
                }
            }

            results.add(copy)
        }

        return results
    }

    private fun transformToCoins(input: CryptoCompareWrapper<CryptoCompareCoinList>): List<Coin> {
        var coins: MutableList<Coin> = mutableListOf()

        for (key in input.data.data) {
            coins.add(Coin(key.value.symbol, key.value.coinName, key.value.sortOrder.toInt(), false))
        }

        coins = coins.sortedWith(compareBy({ it.sortOrder })).toMutableList()

        return coins
    }

    private class CachePriceWrapper(val timestamp: Long, val prices: List<Price>)
}
