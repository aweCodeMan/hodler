package si.betoo.hodler.data.database

import android.arch.persistence.room.RoomDatabase
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Transaction

@android.arch.persistence.room.Database(entities = arrayOf(Coin::class, Transaction::class), version = 3, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun coinDAO(): CoinDAO
    abstract fun transactionDAO(): TransactionDAO
}