package si.betoo.hodler.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Single
import si.betoo.hodler.data.coin.Coin

@Dao
interface CoinDAO {

    @Query("SELECT * FROM coins ORDER BY CAST(sort_order AS NUMERIC) ASC")
    fun getAllCoins(): Single<List<Coin>>

    @Insert(onConflict = REPLACE)
    fun insertMany(coins: List<Coin>)

    @Insert(onConflict = REPLACE)
    fun insert(coin: Coin)

    @Query("SELECT * FROM coins WHERE symbol = :symbol")
    fun find(symbol: String): Single<Coin>

    @Query("SELECT * FROM coins WHERE is_active = 1 ORDER BY CAST(sort_order AS NUMERIC) ASC")
    fun getActiveCoins(): Single<List<Coin>>
}