package si.betoo.cryptocompare

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

internal class CoinPriceDeserializer : JsonDeserializer<CryptoCompareCoinPrice> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CryptoCompareCoinPrice {

        val type = object : TypeToken<Map<String, Double>>() {}.type
        val data: Map<String, Double> = Gson().fromJson(json.toString(), type)

        return CryptoCompareCoinPrice(data)
    }
}