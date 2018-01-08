package si.betoo.hodler.ui.holding

import si.betoo.hodler.data.coin.Holding

interface HoldingFormlMVP {

    interface View {
        fun closeView()
        fun loadHolding(holding: Holding)
    }

    interface Presenter {
        fun saveHolding(symbol: String, amount: Double)
        fun loadHolding(id: Long)
    }
}