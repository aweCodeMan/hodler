package si.betoo.cryptocompare.data.prices

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PriceDisplay(
        @SerializedName("TYPE") val type: String,
        @SerializedName("MARKET") val market: String,
        @SerializedName("FROMSYMBOL") val fromSymbol: String,
        @SerializedName("TOSYMBOL") val tosSmbol: String,
        @SerializedName("FLAGS") val flags: String,
        @SerializedName("PRICE") val price: String,
        @SerializedName("LASTUPDATE") val lastUpdate: String,
        @SerializedName("LASTVOLUME") val lastVolume: String,
        @SerializedName("LASTVOLUMETO") val lastVolumeTo: String,
        @SerializedName("LASTTRADEID") val lastTradeId: String,
        @SerializedName("VOLUMEDAY") val volumeDay: String,
        @SerializedName("VOLUMEDAYTO") val volumeDayTo: String,
        @SerializedName("VOLUME24HOUR") val volume24Hour: String,
        @SerializedName("VOLUME24HOURTO") val volume24HourTo: String,
        @SerializedName("OPENDAY") val openDay: String,
        @SerializedName("HIGHDAY") val highDay: String,
        @SerializedName("LOWDAY") val lowDay: String,
        @SerializedName("OPEN24HOUR") val open24Hour: String,
        @SerializedName("HIGH24HOUR") val high24Hour: String,
        @SerializedName("LOW24HOUR") val low24Hour: String,
        @SerializedName("LASTMARKET") val lastMarket: String,
        @SerializedName("CHANGE24HOUR") val change24Hour: String,
        @SerializedName("CHANGEPCT24HOUR") val changePercent24Hour: Double,
        @SerializedName("CHANGEDAY") val changeDay: String,
        @SerializedName("CHANGEPCTDAY") val changePercentDay: String,
        @SerializedName("SUPPLY") val supply: String,
        @SerializedName("MKTCAP") val marketCap: String,
        @SerializedName("TOTALVOLUME24H") val totalVolume24Hour: String,
        @SerializedName("TOTALVOLUME24HTO") val totalVolume24HourTo: String
)