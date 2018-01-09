package si.betoo.cryptocompare.data.prices

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PriceRaw(
        @SerializedName("TYPE") val type: String,
        @SerializedName("MARKET") val market: String,
        @SerializedName("FROMSYMBOL") val fromSymbol: String,
        @SerializedName("TOSYMBOL") val tosSmbol: String,
        @SerializedName("FLAGS") val flags: String,
        @SerializedName("PRICE") val price: Double,
        @SerializedName("LASTUPDATE") val lastUpdate: Double,
        @SerializedName("LASTVOLUME") val lastVolume: Double,
        @SerializedName("LASTVOLUMETO") val lastVolumeTo: Double,
        @SerializedName("LASTTRADEID") val lastTradeId: String,
        @SerializedName("VOLUMEDAY") val volumeDay: Double,
        @SerializedName("VOLUMEDAYTO") val volumeDayTo: Double,
        @SerializedName("VOLUME24HOUR") val volume24Hour: Double,
        @SerializedName("VOLUME24HOURTO") val volume24HourTo: Double,
        @SerializedName("OPENDAY") val openDay: Double,
        @SerializedName("HIGHDAY") val highDay: Double,
        @SerializedName("LOWDAY") val lowDay: Double,
        @SerializedName("OPEN24HOUR") val open24Hour: Double,
        @SerializedName("HIGH24HOUR") val high24Hour: Double,
        @SerializedName("LOW24HOUR") val low24Hour: Double,
        @SerializedName("LASTMARKET") val lastMarket: String,
        @SerializedName("CHANGE24HOUR") val change24Hour: Double,
        @SerializedName("CHANGEPCT24HOUR") val changePercent24Hour: Double,
        @SerializedName("CHANGEDAY") val changeDay: Double,
        @SerializedName("CHANGEPCTDAY") val changePercentDay: Double,
        @SerializedName("SUPPLY") val supply: Double,
        @SerializedName("MKTCAP") val marketCap: Double,
        @SerializedName("TOTALVOLUME24H") val totalVolume24Hour: Double,
        @SerializedName("TOTALVOLUME24HTO") val totalVolume24HourTo: Double
)