package si.betoo.hodler.data.cryptocompare

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CryptoComparePrice(
        @SerializedName("TYPE") val TYPE: String,
        @SerializedName("MARKET") val MARKET: String,
        @SerializedName("FROMSYMBOL") val FROMSYMBOL: String,
        @SerializedName("TOSYMBOL") val TOSYMBOL: String,
        @SerializedName("FLAGS") val FLAGS: String,
        @SerializedName("PRICE") val PRICE: String,
        @SerializedName("LASTUPDATE") val LASTUPDATE: String,
        @SerializedName("LASTVOLUME") val LASTVOLUME: String,
        @SerializedName("LASTVOLUMETO") val LASTVOLUMETO: String,
        @SerializedName("LASTTRADEID") val LASTTRADEID: String,
        @SerializedName("VOLUMEDAY") val VOLUMEDAY: String,
        @SerializedName("VOLUMEDAYTO") val VOLUMEDAYTO: String,
        @SerializedName("VOLUME24HOUR") val VOLUME24HOUR: String,
        @SerializedName("VOLUME24HOURTO") val VOLUME24HOURTO: String,
        @SerializedName("OPENDAY") val OPENDAY: String,
        @SerializedName("HIGHDAY") val HIGHDAY: String,
        @SerializedName("LOWDAY") val LOWDAY: String,
        @SerializedName("OPEN24HOUR") val OPEN24HOUR: String,
        @SerializedName("HIGH24HOUR") val HIGH24HOUR: String,
        @SerializedName("LOW24HOUR") val LOW24HOUR: String,
        @SerializedName("LASTMARKET") val LASTMARKET: String,
        @SerializedName("CHANGE24HOUR") val CHANGE24HOUR: String,
        @SerializedName("CHANGEPCT24HOUR") val CHANGEPCT24HOUR: Double,
        @SerializedName("CHANGEDAY") val CHANGEDAY: String,
        @SerializedName("CHANGEPCTDAY") val CHANGEPCTDAY: String,
        @SerializedName("SUPPLY") val SUPPLY: String,
        @SerializedName("MKTCAP") val MKTCAP: String,
        @SerializedName("TOTALVOLUME24H") val TOTALVOLUME24H: String,
        @SerializedName("TOTALVOLUME24HTO") val TOTALVOLUME24HTO: String
)