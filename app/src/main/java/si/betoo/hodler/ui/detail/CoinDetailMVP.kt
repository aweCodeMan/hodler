package si.betoo.hodler.ui.detail

import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Holding
import si.betoo.hodler.data.coin.Price

interface CoinDetailMVP {

    interface View {
        fun showCoin(coin: Coin)
        fun showHoldings(holdings: List<Holding>)

        fun showHoldingForm(coin: Coin, holding: Holding)
        fun showHoldingForm(coin: Coin)
        fun showPrices(prices: List<Price>)
    }

    interface Presenter {
        fun onCreate(symbol: String)
        fun onAddHoldingClicked()
        fun onHoldingClicked(item: Holding)
    }
}