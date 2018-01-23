package si.betoo.hodler.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.*
import si.betoo.hodler.roundTo2DecimalPlaces
import timber.log.Timber

class MainPresenter(private var view: MainMVP.View,
                    private val coinService: CoinService) : MainMVP.Presenter {

    var currencyIndex = 0
    var cachedPrices: List<CoinWithPrices> = ArrayList()
    var cachedCoins: List<CoinWithTransactions> = ArrayList()

    override fun onCreate() {
        coinService.getActiveCoinsWithTransactions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coins ->
                    cachedCoins = coins
                    view.showCoins(coins.map { CoinWithPrices(it, HashMap()) })

                    loadPricesForCoins(coins)
                }, { error -> Timber.e(error) })
    }

    override fun onAddCoinsClicked() {
        view.showAddCoinsScreen()
    }

    override fun onCoinClicked(coin: Coin) {
        view.showCoinDetailScreen(coin)
    }

    override fun refreshPrices() {
        loadPricesForCoins(cachedCoins)
    }

    override fun switchCurrentCurrency() {
        currencyIndex++

        if (currencyIndex >= coinService.availableCurrencies.size) {
            currencyIndex = 0
        }

        calculateTotalValue(cachedPrices)
        view.updatePrices(cachedPrices, getCurrentCurrencyCode())
    }

    override fun onSettingsClicked() {
        view.showSettingsScreen()
    }

    private fun loadPricesForCoins(coins: List<CoinWithTransactions>) {
        if (coins.isNotEmpty()) {
            val symbols = coins.joinToString { item -> item.coin.symbol }.replace(" ", "")

            view.showProgress(true)

            coinService.getPricesForCoins(symbols)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ prices ->
                        val updatedCoins = mergeCoinsWithPrices(coins, prices)
                        cachedPrices = updatedCoins

                        view.updatePrices(updatedCoins, getCurrentCurrencyCode())
                        view.showProgress(false)

                        calculateTotalValue(updatedCoins)
                    }, { error -> Timber.e(error) })
        }
    }

    private fun calculateTotalValue(updatedCoins: List<CoinWithPrices>) {

        var total = 0.0
        val currency = getCurrentCurrencyCode()

        for (updatedCoin in updatedCoins) {
            val amount = updatedCoin.holdingsAmount()

            if (updatedCoin.prices[currency] != null) {
                //  Don't convert if we're showing the total for a crypto currency (like BTC,ETH)
                if (updatedCoin.coin.coin.symbol == currency) {
                    total += amount
                } else {
                    total += updatedCoin.prices[currency]!!.price.times(amount)
                }
            }
        }

        var percentChange = 0.0

        //  Calculate percentage drop and total value drop
        for (updatedCoin in updatedCoins) {
            if (updatedCoin.prices[currency] != null) {
                if (updatedCoin.coin.coin.symbol != currency) {

                    val coinChange = updatedCoin.prices[currency]!!.change24HourPercent
                    percentChange += coinChange * (updatedCoin.prices[currency]!!.price.times(updatedCoin.holdingsAmount()) / total)
                }
            }
        }

        view.showPercentChange(percentChange)
        view.showTotalChange(percentChange * total / 100, getCurrentCurrencyCode(), coinService.availableCurrencies[getCurrentCurrencyCode()]!!)
        view.showTotal(total.roundTo2DecimalPlaces(), getCurrentCurrencyCode(), coinService.availableCurrencies[getCurrentCurrencyCode()]!!)
    }

    private fun mergeCoinsWithPrices(coins: List<CoinWithTransactions>, prices: List<Price>): List<CoinWithPrices> {
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

    private fun getCurrentCurrencyCode(): String {
        return coinService.availableCurrencies.keys.elementAt(currencyIndex)
    }
}