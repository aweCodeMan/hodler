package si.betoo.cryptocompare.data

import com.google.gson.annotations.SerializedName

class History(
        @SerializedName("time") val time: Long,
        @SerializedName("close") val close: Double,
        @SerializedName("high") val high: Double,
        @SerializedName("low") val low: Double,
        @SerializedName("open") val open: Double,
        @SerializedName("volumefrom") val volumeFrom: Double,
        @SerializedName("volumeto") val volumeTo: Double
)