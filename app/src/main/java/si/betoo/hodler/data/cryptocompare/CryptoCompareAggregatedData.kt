package si.betoo.hodler.data.cryptocompare

import com.google.gson.annotations.SerializedName

data class CryptoCompareAggregatedData(
        @SerializedName("TYPE") val id: String,
        @SerializedName("MARKET") val Url: String,
        @SerializedName("FROMSYMBOL") private var _imageUrl: String,
        @SerializedName("TOSYMBOL") val Name: String,
        @SerializedName("FLAGS") val Symbol: String,
        @SerializedName("PRICE") val CoinName: String,
        @SerializedName("LASTUPDATE") val FullName: String,
        @SerializedName("LASTVOLUME") val Algorithm: String,
        @SerializedName("LASTVOLUMETO") val ProofType: String,
        @SerializedName("LASTTRADEID") val FullyPremined: String,
        @SerializedName("VOLUME24HOUR") val TotalCoinSupply: String,
        @SerializedName("VOLUME24HOURTO") val PreMinedValue: String,
        @SerializedName("VOLUMEDAY") val VOLUMEDAY: String,
        @SerializedName("VOLUMEDAYTO") val VOLUMEDAYTO: String,
        @SerializedName("OPEN24HOUR") val TotalCoinsFreeFloat: String,
        @SerializedName("HIGH24HOUR") val SortOrder: String,
        @SerializedName("LOW24HOUR") val Sponsored: String,
        @SerializedName("OPENDAY") val OPENDAY: String,
        @SerializedName("HIGHDAY") val HIGHDAY: String,
        @SerializedName("LOWDAY") val LOWDAY: String,
        @SerializedName("LASTMARKET") val LASTMARKET: String
)