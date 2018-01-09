package si.betoo.cryptocompare.data

import com.google.gson.annotations.SerializedName

data class Coin(@SerializedName("Id") val id: String,
                @SerializedName("Url") val url: String,
                @SerializedName("ImageUrl") private var imageUrl: String,
                @SerializedName("Name") val name: String,
                @SerializedName("Symbol") val symbol: String,
                @SerializedName("CoinName") val coinName: String,
                @SerializedName("FullName") val fullName: String,
                @SerializedName("Algorithm") val algorithm: String,
                @SerializedName("ProofType") val proofType: String,
                @SerializedName("FullyPremined") val fullyPremined: String,
                @SerializedName("TotalCoinSupply") val totalCoinSupply: String,
                @SerializedName("PreMinedValue") val preMinedValue: String,
                @SerializedName("TotalCoinsFreeFloat") val totalCoinsFreeFloat: String,
                @SerializedName("SortOrder") val sortOrder: String,
                @SerializedName("Sponsored") val sponsored: String
)