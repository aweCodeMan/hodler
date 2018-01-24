package si.betoo.hodler.data.coin

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import java.util.*

@Entity(tableName = "prices", primaryKeys = ["coin_code", "currency_code"])
data class Price
(
        @ColumnInfo(name = "coin_code")
        val coinSymbol: String,
        @ColumnInfo(name = "currency_code")
        val currency: String,
        @ColumnInfo(name = "currency_symbol")
        val currencySymbol: String,
        @ColumnInfo(name = "price")
        val price: Double,
        @ColumnInfo(name = "change_24_hour_percent")
        val change24HourPercent: Double,
        @ColumnInfo(name = "updated_at")
        val lastUpdate: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as Price
        return coinSymbol == that.coinSymbol && currency == that.currency && lastUpdate == that.lastUpdate
    }

    override fun hashCode(): Int {
        return Objects.hash(coinSymbol, currency)
    }
}