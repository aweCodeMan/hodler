package si.betoo.hodler.ui.select

import si.betoo.hodler.data.coin.Coin

interface SelectCoinsMVP {

    interface View {
        fun showAvailableCoins(coins: List<Coin>)
    }

    interface Presenter {
        fun onCreate()
        fun onCoinToggle(item: Coin)
        fun onResume()
    }
}