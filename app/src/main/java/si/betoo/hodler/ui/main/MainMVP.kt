package si.betoo.hodler.ui.main

import si.betoo.hodler.data.coin.Coin

interface MainMVP {

    interface View {
        fun showAddScreen()
        fun showCoins(coins: List<CoinWithPrices>)
        fun showProgress(show: Boolean)
        fun updatePrices(prices: List<CoinWithPrices>, currencyCode: String)
        fun showCoinDetail(coin: Coin)
        fun showTotal(total: Double, currencyCode: String, currencySymbol: String)
        fun showSettings()
    }

    interface Presenter {
        fun onCreate()
        fun onAddClicked()
        fun onCoinClicked(coin: Coin)
        fun switchCurrentCurrency()
        fun refreshPrices()
        fun onSettingsClicked()
    }
}