package si.betoo.hodler.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Single
import si.betoo.hodler.data.coin.Transaction

@Dao
interface TransactionDAO {

   /* @Query("SELECT * FROM coins ORDER BY CAST(sort_order AS NUMERIC) ASC")
    fun getAllCoins(): Single<List<Coin>>*/

    @Insert(onConflict = REPLACE)
    fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE symbol = :symbol")
    fun findBySymbol(symbol: String): Single<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun find(id: Long): Single<Transaction>

    @Delete
    fun delete(transaction: Transaction)

    /*@Query("SELECT * FROM coins WHERE is_active = 1 ORDER BY CAST(sort_order AS NUMERIC) ASC")
    fun getActiveCoins(): Single<List<Coin>>*/
}