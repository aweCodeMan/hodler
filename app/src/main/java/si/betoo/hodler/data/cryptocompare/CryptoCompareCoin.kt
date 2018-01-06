package si.betoo.hodler.data.cryptocompare

import com.google.gson.annotations.SerializedName

data class CryptoCompareCoin(@SerializedName("Id") val id: String,
                             @SerializedName("Url") val Url: String,
                             @SerializedName("ImageUrl") private var _imageUrl: String,
                             @SerializedName("Name") val name: String,
                             @SerializedName("Symbol") val symbol: String,
                             @SerializedName("CoinName") val coinName: String,
                             @SerializedName("FullName") val FullName: String,
                             @SerializedName("Algorithm") val Algorithm: String,
                             @SerializedName("ProofType") val ProofType: String,
                             @SerializedName("FullyPremined") val FullyPremined: String,
                             @SerializedName("TotalCoinSupply") val TotalCoinSupply: String,
                             @SerializedName("PreMinedValue") val PreMinedValue: String,
                             @SerializedName("TotalCoinsFreeFloat") val TotalCoinsFreeFloat: String,
                             @SerializedName("SortOrder") val sortOrder: String,
                             @SerializedName("Sponsored") val Sponsored: String
)