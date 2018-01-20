package si.betoo.hodler.ui.transaction

import si.betoo.hodler.data.coin.Transaction

interface TransactionFormMVP {

    interface View {
        fun closeView()
        fun showTransactionData(transaction: Transaction)
        fun showDeleteTransactionConfirmation(transaction: Transaction)
        fun loadExchanges(exchanges: Map<String, List<String>>, transaction: Transaction)
        fun showProgress(show: Boolean)
        fun enableSubmit(enable: Boolean)
    }

    interface Presenter {
        fun saveTransaction()
        fun setupTransactionData(symbol: String, transactionId: Long)
        fun onDeleteTransactionClicked()
        fun removeTransaction(transaction: Transaction)
        fun canTransactionBeDeleted(): Boolean
        fun onDateTimeChanged(millis: Long)
        fun onTransactionTypeChanged(transactionType: Int)
        fun onExchangeSelected(exchange: String)
        fun onExchangePairSelected(pair: String)
        fun onAmountChanged(amount: String)
        fun onPricePerUnitChange(pricePerUnit: String)
        fun onPriceTotalChanged(priceTotal: String)
    }
}