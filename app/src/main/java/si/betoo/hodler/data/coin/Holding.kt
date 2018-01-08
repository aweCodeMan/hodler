package si.betoo.hodler.data.coin

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "holdings")
data class Holding(

        @ColumnInfo(name = "symbol")
        val symbol: String,
        @ColumnInfo(name = "amount")
        var amount: Double) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
}