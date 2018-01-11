package si.betoo.hodler.data.coin

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.database.Database

class TransactionService(private var database: Database) {
    fun saveTransaction(transaction: Transaction) {
        Observable.just(database)
                .subscribeOn(Schedulers.io())
                .subscribe { db -> db.transactionDAO().insert(transaction) }
    }

    fun getTransactionsForCoin(symbol: String): Observable<List<Transaction>> {
        return database.transactionDAO()
                .findBySymbol(symbol)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .share()
    }

    fun find(id: Long): Observable<Transaction> {
        return database.transactionDAO()
                .find(id)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .share()
    }

    fun remove(transaction: Transaction) {
        Observable.just(database)
                .subscribeOn(Schedulers.io())
                .subscribe { db -> db.transactionDAO().delete(transaction) }    }
}
