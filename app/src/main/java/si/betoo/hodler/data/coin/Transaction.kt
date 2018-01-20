package si.betoo.hodler.data.coin

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
        @ColumnInfo(name = "currency_code")
        val currencyCode: String,
        @ColumnInfo(name = "type")
        var type: Int = Transaction.TYPE_BUY,
        @ColumnInfo(name = "amount")
        var amount: Double,
        @ColumnInfo(name = "price_per_unit")
        var pricePerUnit: Double,
        @ColumnInfo(name = "total_price")
        var totalPrice: Double,
        @ColumnInfo(name = "exchange")
        var exchange: String?,
        @ColumnInfo(name = "exchange_pair_symbol")
        var exchangePairSymbol: String?,
        @ColumnInfo(name = "created_at")
        var createdAtMillis: Long) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    companion object {
        const val TYPE_SELL = 0
        const val TYPE_BUY = 1
    }
}