package si.betoo.hodler.data.database

import android.arch.persistence.room.RoomDatabase
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Holding

@android.arch.persistence.room.Database(entities = arrayOf(Coin::class, Holding::class), version = 3, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun coinDAO(): CoinDAO
    abstract fun holdingDAO(): HoldingDAO
}