package si.betoo.hodler.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.reactivex.Maybe
import io.reactivex.Single
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.CoinWithTransactions
import si.betoo.hodler.data.coin.Price

@Dao
interface PriceDAO {

    @Query("SELECT * FROM prices WHERE coin_code IN (:symbols) AND currency_code IN (:currencies)")
    fun getPrices(symbols: Array<String>, currencies: Array<String>): Maybe<List<Price>>

    @Insert(onConflict = REPLACE)
    fun insertMany(coins: List<Price>)

    @Insert(onConflict = REPLACE)
    fun insert(coin: Price)
}