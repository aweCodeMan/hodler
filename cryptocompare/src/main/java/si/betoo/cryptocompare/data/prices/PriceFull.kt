package si.betoo.cryptocompare.data.prices

import com.google.gson.annotations.SerializedName

data class PriceFull(@SerializedName("RAW") val raw: Map<String, PriceMultiRaw>,
                     @SerializedName("DISPLAY") val display: Map<String, PriceMultiDisplay>)