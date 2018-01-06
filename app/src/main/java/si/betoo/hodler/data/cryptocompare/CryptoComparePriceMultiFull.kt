package si.betoo.hodler.data.cryptocompare

import com.google.gson.annotations.SerializedName

data class CryptoComparePriceMultiFull(@SerializedName("RAW") val raw: Map<String, CryptoComparePriceMulti>, @SerializedName("DISPLAY") val display: Map<String, CryptoComparePriceMulti>) {

}