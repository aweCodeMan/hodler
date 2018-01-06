package si.betoo.hodler.ui.main

import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Price

interface MainMVP {

    interface View {
        fun showAddScreen()
        fun showCoins(coins: List<CoinWithPrices>)
        fun showProgress(show: Boolean)
        fun updatePrices(prices: List<CoinWithPrices>)
    }

    interface Presenter {
        fun onCreate()
        fun onAddClicked()
    }

}