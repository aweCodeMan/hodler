package si.betoo.hodler.data.coin

import android.arch.persistence.room.*

@Entity(tableName = "coins")
data class Coin
(
        @PrimaryKey()
        @ColumnInfo(name = "symbol")
        val symbol: String,

        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "sort_order")
        val sortOrder: Int,

        @ColumnInfo(name = "is_active")
        var isActive: Boolean
)