package si.betoo.hodler.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.reactivex.Single
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.CoinWithTransactions

@Dao
interface CoinDAO {

    @Query("SELECT * FROM coins ORDER BY CAST(sort_order AS NUMERIC) ASC")
    fun getAllCoins(): Single<List<Coin>>

    @Insert(onConflict = REPLACE)
    fun insertMany(coins: List<Coin>)

    @Insert(onConflict = REPLACE)
    fun insert(coin: Coin)

    @Query("SELECT * FROM coins WHERE currency_code = :currencyCode")
    fun find(currencyCode: String): Single<Coin>

    @Query("SELECT * FROM coins WHERE is_active = 1 ORDER BY CAST(sort_order AS NUMERIC) ASC")
    fun getActiveCoins(): Single<List<Coin>>

    @Transaction
    @Query("SELECT * FROM coins WHERE is_active = 1 ORDER BY CAST(sort_order AS NUMERIC) ASC")
    fun getActiveCoinsWithHoldings(): Single<List<CoinWithTransactions>>
}