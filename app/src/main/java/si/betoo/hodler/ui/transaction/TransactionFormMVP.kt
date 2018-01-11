package si.betoo.hodler.ui.transaction

import si.betoo.hodler.data.coin.Transaction

interface TransactionFormMVP {

    interface View {
        fun closeView()
        fun loadHolding(transaction: Transaction)
        fun showDeleteConfirmation(transaction: Transaction)
    }

    interface Presenter {
        fun saveHolding(symbol: String, amount: Double)
        fun loadHolding(id: Long)
        fun onDeleteClicked()
        fun removeTransaction(transaction: Transaction)
    }
}