package si.betoo.hodler.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.CoinService
import si.betoo.hodler.data.coin.HoldingService
import si.betoo.hodler.data.coin.Price
import timber.log.Timber

class MainPresenter(private var view: MainMVP.View,
                    private val coinService: CoinService,
                    private val holdingService: HoldingService) : MainMVP.Presenter {


    override fun onCreate() {
        coinService.getActiveCoins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coins ->
                    view.showCoins(coins.map { CoinWithPrices(it, HashMap()) })
                    view.showProgress(false)

                    loadHoldings(coins)

                    loadPricesForCoins(coins)
                }, { error -> Timber.e(error) })
    }

    private fun loadHoldings(coins: List<Coin>) {
        for (coin in coins) {
            holdingService.getHoldingsForCoin(coin.symbol)
        }
    }


    private fun loadPricesForCoins(coins: List<Coin>) {
        if (coins.isNotEmpty()) {
            val symbols = coins.joinToString { item -> item.symbol }.replace(" ", "")

            coinService.getPricesForCoins(symbols)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ prices ->
                        val updatedCoins = mergeCoinsWithPrices(coins, prices)
                        view.updatePrices(updatedCoins)
                    }, { error -> Timber.e(error) })
        }
    }

    private fun mergeCoinsWithPrices(coins: List<Coin>, prices: List<Price>): List<CoinWithPrices> {
        val results: MutableList<CoinWithPrices> = ArrayList(coins.size)

        for (coin in coins) {
            val coinPrices: MutableMap<String, Price> = HashMap()

            for (price in prices) {
                if (price.coinSymbol.toLowerCase() == coin.symbol.toLowerCase()) {
                    coinPrices.put(price.currency, price)
                }
            }

            results.add(CoinWithPrices(coin, coinPrices))
        }

        return results
    }

    override fun onAddClicked() {
        view.showAddScreen()
    }

    override fun onCoinClicked(coin: Coin) {
        view.showCoinDetail(coin)
    }
}