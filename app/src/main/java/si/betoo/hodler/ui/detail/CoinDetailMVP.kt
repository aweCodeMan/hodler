package si.betoo.hodler.ui.detail

import si.betoo.cryptocompare.data.History
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Transaction
import si.betoo.hodler.data.coin.Price

interface CoinDetailMVP {

    interface View {
        fun showCoin(coin: Coin)
        fun showHoldings(transactions: List<Transaction>)

        fun showHoldingForm(coin: Coin, transaction: Transaction)
        fun showHoldingForm(coin: Coin)
        fun showHistory(history: PriceHistory)
    }

    interface Presenter {
        fun onCreate(symbol: String)
        fun onAddHoldingClicked()
        fun onHoldingClicked(item: Transaction)
        fun onChartClicked()
    }
}