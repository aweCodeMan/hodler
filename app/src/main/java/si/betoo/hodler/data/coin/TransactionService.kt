package si.betoo.hodler.data.coin

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import si.betoo.cryptocompare.CryptoCompare
import si.betoo.cryptocompare.data.AllExchanges
import si.betoo.hodler.data.database.Database

class TransactionService(private var database: Database, private val cryptoCompare: CryptoCompare) {

    private var cachedExchanges: AllExchanges? = null


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
                .subscribe { db -> db.transactionDAO().delete(transaction) }
    }

    fun getAllExchangesForSymbol(symbol: String): Observable<Map<String, List<String>>> {

        if (cachedExchanges != null) {
            val results = extractExchangeForSymbol(symbol, cachedExchanges)

            return Observable.just(results)
        }

        return cryptoCompare.getAllExchanges().map { exchanges ->
            cachedExchanges = exchanges
            val results = extractExchangeForSymbol(symbol, cachedExchanges)

            results
        }
    }

    private fun extractExchangeForSymbol(symbol: String, cachedExchanges: AllExchanges?): Map<String, List<String>> {
        val result: MutableMap<String, List<String>> = HashMap()

        if (cachedExchanges != null) {
            for (exchange in cachedExchanges.data) {
                val exchangeName = exchange.key

                for (exchangePairs in exchange.value) {
                    if(symbol == exchangePairs.key)
                    {
                        result.put(exchangeName, exchangePairs.value)
                    }
                }
            }
        }

        return result
    }
}
