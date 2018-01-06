package si.betoo.hodler.data.database

import android.arch.persistence.room.RoomDatabase
import si.betoo.hodler.data.coin.Coin

@android.arch.persistence.room.Database(entities = arrayOf(Coin::class), version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun coinDAO(): CoinDAO
}