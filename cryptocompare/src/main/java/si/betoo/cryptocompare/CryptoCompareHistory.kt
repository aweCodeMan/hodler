package si.betoo.cryptocompare

import com.google.gson.annotations.SerializedName

class CryptoCompareHistory(
        @SerializedName("time") val time: Long,
        @SerializedName("close") val close: Double,
        @SerializedName("high") val high: Double,
        @SerializedName("low") val low: Double,
        @SerializedName("open") val open: Double,
        @SerializedName("volumefrom") val volumefrom: Double,
        @SerializedName("volumeto") val volumeto: Double
)