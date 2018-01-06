package si.betoo.hodler.ui.main

import si.betoo.hodler.data.coin.Coin

interface MainMVP {

    interface View {
        fun showAddScreen()
        fun showCoins(coins: List<Coin>)
        fun showProgress(show: Boolean)
    }

    interface Presenter {
        fun onCreate()
        fun onAddClicked()
    }

}