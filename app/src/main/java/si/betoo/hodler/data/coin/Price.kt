package si.betoo.hodler.data.coin

import java.util.*

data class Price
(
        val coinSymbol: String,
        val currency: String,
        val price: String,
        val change24HourPercent: Double,
        val lastUpdate: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as Price
        return coinSymbol == that.coinSymbol && currency == that.currency
    }

    override fun hashCode(): Int {
        return Objects.hash(coinSymbol, currency)
    }
}