package si.betoo.cryptocompare.deserializers

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import si.betoo.cryptocompare.data.prices.PriceMap
import java.lang.reflect.Type

internal class PriceDeserializer : JsonDeserializer<PriceMap> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): PriceMap {

        val type = object : TypeToken<Map<String, Double>>() {}.type
        val data: Map<String, Double> = Gson().fromJson(json.toString(), type)

        return PriceMap(data)
    }
}