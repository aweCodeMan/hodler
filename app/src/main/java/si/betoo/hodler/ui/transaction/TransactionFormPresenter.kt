package si.betoo.hodler.ui.transaction

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.Transaction
import si.betoo.hodler.data.coin.TransactionService
import timber.log.Timber

class TransactionFormPresenter(private var view: TransactionFormMVP.View, private val transactionService: TransactionService) : TransactionFormMVP.Presenter {
    private lateinit var transaction: Transaction

    private var exchangesForSymbol: Map<String, List<String>>? = null

    override fun setupTransactionData(symbol: String, transactionId: Long) {
        if (transactionId >= 0) {
            transactionService.find(transactionId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ transaction ->
                        this.transaction = transaction

                        view.showTransactionData(transaction)
                    }, { error -> Timber.e(error) })
        } else {
            this.transaction = createEmptyTransaction(symbol)
            view.showTransactionData(this.transaction!!)
        }

        loadExchanges(symbol)
    }

    private fun loadExchanges(symbol: String) {
        view.showProgress(true)
        transactionService.getAllExchangesForSymbol(symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ exchanges ->
                    view.showProgress(false)

                    exchangesForSymbol = exchanges
                    view.loadExchanges(exchanges, transaction)

                }, { error -> Timber.e(error) })
    }

    override fun saveTransaction() {
        transactionService.saveTransaction(transaction)
        view.closeView()
    }

    override fun onDeleteTransactionClicked() {
        view.showDeleteTransactionConfirmation(transaction!!)
    }

    override fun removeTransaction(transaction: Transaction) {
        transactionService.remove(transaction)
        view.closeView()
    }

    override fun onDateTimeChanged(millis: Long) {
        transaction.createdAtMillis = millis
    }

    override fun canTransactionBeDeleted(): Boolean {
        if (transaction.id != null) {
            return transaction.id!! > 0
        }

        return false
    }

    override fun onTransactionTypeChanged(transactionType: Int) {
        transaction.type = transactionType
    }

    override fun onExchangeSelected(exchange: String) {
        transaction.exchange = exchange
    }

    override fun onExchangePairSelected(pair: String) {
        transaction.exchangePairSymbol = pair
    }

    override fun onAmountChanged(amount: String) {
        if (amount.isNotEmpty()) {
            transaction.amount = amount.toDouble()
            view.enableSubmit(true)
        } else {
            transaction.amount = 0.0
            view.enableSubmit(false)
        }
    }

    override fun onPricePerUnitChange(pricePerUnit: String) {
        if (pricePerUnit.isNotEmpty()) {
            transaction.pricePerUnit = pricePerUnit.toDouble()
        } else {
            transaction.pricePerUnit = 0.0
        }
    }

    override fun onPriceTotalChanged(priceTotal: String) {
        if (priceTotal.isNotEmpty()) {
            transaction.totalPrice = priceTotal.toDouble()
        } else {
            transaction.totalPrice = 0.0
        }
    }

    private fun createEmptyTransaction(symbol: String): Transaction = Transaction(symbol, Transaction.TYPE_BUY, 0.0, 0.0, 0.0, "", "", System.currentTimeMillis())
}