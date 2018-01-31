package si.betoo.hodler.data.coin

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import si.betoo.cryptocompare.CryptoCompare
import si.betoo.cryptocompare.data.CoinMap
import si.betoo.cryptocompare.data.History
import si.betoo.cryptocompare.data.Wrapper
import si.betoo.hodler.UserSettings
import si.betoo.hodler.data.database.Database
import timber.log.Timber
import java.util.ArrayList

class CoinService(private val provideCryptoCompare: CryptoCompare, private var database: Database, private var
settingsObserver: UserSettings) {

    var availableCurrencies: Map<String, String> = HashMap()

    init {
        settingsObserver.getUserCurrencies().subscribe({ cu -> availableCurrencies = cu })
    }

    private val coinsFromAPI = ArrayList<Coin>()
    private val observableAvailableCoins = PublishSubject.create<List<Coin>>()

    private val cachedPrices: MutableMap<String, CachePriceWrapper> = HashMap()

    companion object {
        const val PRICE_CACHE_IN_MS = 15000
    }

    fun getActiveCoinsWithTransactions(): Observable<List<CoinWithTransactions>> {
        return database.coinDAO()
                .getActiveCoinsWithHoldings()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .share()
    }

    fun getActiveCoins(): Observable<List<Coin>> {
        return database.coinDAO()
                .getActiveCoins()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .share()
    }

    fun getCoin(symbol: String): Observable<Coin> {
        return database.coinDAO()
                .find(symbol)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .share()
    }

    fun getAvailableCoins(): Observable<List<Coin>> {
        getActiveCoins().subscribe { activeCoins ->
            if (coinsFromAPI.size > 0) {
                observableAvailableCoins.onNext(mergeAvailableWithActive(activeCoins, coinsFromAPI))
            } else {
                provideCryptoCompare
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

    fun getHistoryDay(fromCurrencyCode: String, toCurrencyCode: String, limit: Int): Observable<List<History>> {
        return provideCryptoCompare.getHistoryDay(fromCurrencyCode, toCurrencyCode, limit).map { it.data }
    }

    fun getPricesForCoins(symbols: String, ignoreCache: Boolean = false): Observable<List<Price>> {
        val cachedPrice = cachedPrices[symbols]
        val currencies = availableCurrencies.keys.joinToString(",")

        if (!ignoreCache) {
            if (cachedPrice != null && cachedPrice.timestamp > (System.currentTimeMillis() - PRICE_CACHE_IN_MS)) {
                return Observable.just(cachedPrice.prices)
            }
        }

        val publishSubject = PublishSubject.create<List<Price>>()

        database.priceDAO()
                .getPrices(symbols.split(",").toTypedArray(), availableCurrencies.keys.toTypedArray())
                .subscribeOn(Schedulers.io())
                .doOnError({ Timber.e(it) })
                .toObservable()
                .subscribe({ prices ->
                    if (prices.isNotEmpty() && !ignoreCache) {
                        cachedPrices.put(symbols, CachePriceWrapper(System.currentTimeMillis(), prices))
                        publishSubject.onNext(prices)
                    } else {
                        provideCryptoCompare.getCoinPriceMultiFull(symbols, currencies)
                                .subscribe({ apiPrices ->
                                    val results = ArrayList<Price>()

                                    for (raw in apiPrices.raw) {

                                        for (currency in raw.value.data) {
                                            results.add(Price(raw.key, currency.key, availableCurrencies[currency.key]!!, currency.value.price, currency.value.changePercent24Hour, currency.value.lastUpdate))
                                        }
                                    }

                                    cachedPrices.put(symbols, CachePriceWrapper(System.currentTimeMillis(), results))

                                    Observable.just(database)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe { db -> db.priceDAO().insertMany(results) }

                                    publishSubject.onNext(results)
                                })
                    }
                })

        return publishSubject
    }

    private fun mergeAvailableWithActive(activeCoins: List<Coin>, coinsFromAPI: List<Coin>): List<Coin> {
        var counter = 0

        for (coin in coinsFromAPI) {
            for (activeCoin in activeCoins) {
                if (coin.symbol == activeCoin.symbol) {
                    coin.isActive = activeCoin.isActive
                    counter++
                    break
                }
            }

            if (counter == activeCoins.size) {
                break
            }
        }

        return coinsFromAPI
    }

    private fun transformToCoins(input: Wrapper<CoinMap>): List<Coin> {
        var coins: MutableList<Coin> = mutableListOf()

        for (key in input.data.data) {
            coins.add(Coin(key.value.symbol, key.value.coinName, key.value.sortOrder.toInt(), false))
        }

        coins = coins.sortedWith(compareBy({ it.sortOrder })).toMutableList()

        return coins
    }

    private class CachePriceWrapper(val timestamp: Long, val prices: List<Price>)
}
