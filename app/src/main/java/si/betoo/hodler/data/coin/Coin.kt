package si.betoo.hodler.data.coin

import android.arch.persistence.room.*
import java.util.*

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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as Coin
        return symbol == that.symbol
    }

    override fun hashCode(): Int {
        return Objects.hash(symbol)
    }
}