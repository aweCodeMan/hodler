package si.betoo.cryptocompare.data

import com.google.gson.annotations.SerializedName

data class Wrapper<T>(@SerializedName("Response") val response: String,
                          @SerializedName("Message") val message: String,
                          @SerializedName("BaseImageUrl") val baseImageUrl: String,
                          @SerializedName("BaseLinkUrl") val baseLinkUrl: String,
                          @SerializedName("Data") val data: T)

