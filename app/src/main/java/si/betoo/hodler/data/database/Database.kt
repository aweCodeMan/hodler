package si.betoo.hodler.data.database

import android.arch.persistence.room.RoomDatabase
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Price
import si.betoo.hodler.data.coin.Transaction

@android.arch.persistence.room.Database(entities = [(Coin::class), (Transaction::class), (Price::class)], version = 5, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun coinDAO(): CoinDAO
    abstract fun transactionDAO(): TransactionDAO
    abstract fun priceDAO(): PriceDAO
}