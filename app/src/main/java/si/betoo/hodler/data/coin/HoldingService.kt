package si.betoo.hodler.data.coin

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import si.betoo.hodler.data.database.Database
import si.betoo.hodler.data.api.CryptoCompareAPI
import si.betoo.hodler.data.cryptocompare.CryptoCompareCoinList
import si.betoo.hodler.data.cryptocompare.CryptoCompareWrapper
import timber.log.Timber
import java.util.ArrayList

class HoldingService(private var database: Database) {
    fun saveHolding(holding: Holding) {
        Observable.just(database)
                .subscribeOn(Schedulers.io())
                .subscribe { db -> db.holdingDAO().insert(holding) }
    }

    fun getHoldingsForCoin(symbol: String): Observable<List<Holding>> {
        return database.holdingDAO()
                .findBySymbol(symbol)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .share()
    }

    fun find(id: Long): Observable<Holding> {
        return database.holdingDAO()
                .find(id)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .share()
    }
}
