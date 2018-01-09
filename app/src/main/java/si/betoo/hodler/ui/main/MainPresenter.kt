package si.betoo.hodler.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.*
import timber.log.Timber

class MainPresenter(private var view: MainMVP.View,
                    private val coinService: CoinService,
                    private val holdingService: HoldingService) : MainMVP.Presenter {

    override fun onCreate() {
        coinService.getActiveCoinsWithHoldings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coins ->
                    view.showCoins(coins.map { CoinWithPrices(it, HashMap()) })
                    view.showProgress(false)

                    loadPricesForCoins(coins)
                }, { error -> Timber.e(error) })
    }

    override fun onAddClicked() {
        view.showAddScreen()
    }

    override fun onCoinClicked(coin: Coin) {
        view.showCoinDetail(coin)
    }


    private fun loadPricesForCoins(coins: List<CoinWithHoldings>) {
        if (coins.isNotEmpty()) {
            val symbols = coins.joinToString { item -> item.coin.symbol }.replace(" ", "")

            coinService.getPricesForCoins(symbols)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ prices ->
                        val updatedCoins = mergeCoinsWithPrices(coins, prices)
                        view.updatePrices(updatedCoins)

                        calculateTotalValue(updatedCoins)
                    }, { error -> Timber.e(error) })
        }
    }

    private fun calculateTotalValue(updatedCoins: List<CoinWithPrices>) {
        val currency = "EUR"
        var total = 0.0


        for (updatedCoin in updatedCoins) {
            var amount = 0.0
            updatedCoin.coin.holdings.forEach { amount += it.amount }

            if (updatedCoin.prices[currency] != null) {
                total += updatedCoin.prices[currency]!!.price.times(amount)
            }
        }

        view.showTotal(total, currency)
    }

    private fun mergeCoinsWithPrices(coins: List<CoinWithHoldings>, prices: List<Price>): List<CoinWithPrices> {
        val results: MutableList<CoinWithPrices> = ArrayList(coins.size)

        for (coinWithHoldings in coins) {
            val coinPrices: MutableMap<String, Price> = HashMap()

            for (price in prices) {
                if (price.coinSymbol.toLowerCase() == coinWithHoldings.coin.symbol.toLowerCase()) {
                    coinPrices.put(price.currency, price)
                }
            }

            results.add(CoinWithPrices(coinWithHoldings, coinPrices))
        }

        return results
    }
}