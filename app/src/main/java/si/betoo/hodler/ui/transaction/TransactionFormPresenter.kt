package si.betoo.hodler.ui.transaction

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.Transaction
import si.betoo.hodler.data.coin.TransactionService
import timber.log.Timber

class TransactionFormPresenter(private var view: TransactionFormMVP.View, private val transactionService: TransactionService) : TransactionFormMVP.Presenter {


    private var transaction: Transaction? = null

    override fun loadHolding(id: Long) {
        transactionService.find(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ holding ->

                    this.transaction = holding
                    view.loadHolding(holding)
                }, { error -> Timber.e(error) })

    }

    override fun saveHolding(symbol: String, amount: Double) {
        if (transaction != null) {
            transaction!!.amount = amount
            transactionService.saveTransaction(transaction!!)
        } else {
            transactionService.saveTransaction(Transaction(symbol, amount))
        }
        view.closeView()
    }


    override fun onDeleteClicked() {
        if (transaction != null) {
            view.showDeleteConfirmation(transaction!!)
        }
    }

    override fun removeTransaction(transaction: Transaction) {
        transactionService.remove(transaction)
        view.closeView()
    }
}