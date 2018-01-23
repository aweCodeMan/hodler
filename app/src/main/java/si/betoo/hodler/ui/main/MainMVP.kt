package si.betoo.hodler.ui.main

import si.betoo.hodler.data.coin.Coin

interface MainMVP {

    interface View {
        fun showAddCoinsScreen()
        fun showSettingsScreen()
        fun showCoinDetailScreen(coin: Coin)

        fun updatePrices(prices: List<CoinWithPrices>, currencyCode: String)
        fun showCoins(coins: List<CoinWithPrices>)
        fun showProgress(show: Boolean)

        fun showTotal(total: Double, currencyCode: String, currencySymbol: String)
        fun showPercentChange(percentChange: Double)
        fun showTotalChange(totalChange: Double, currencyCode: String, currencySymbol: String)
    }

    interface Presenter {
        fun onCreate()
        fun refreshPrices()
        fun switchCurrentCurrency()
        fun onCoinClicked(coin: Coin)
        fun onAddCoinsClicked()
        fun onSettingsClicked()
    }
}