package si.betoo.cryptocompare.deserializers

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import si.betoo.cryptocompare.data.prices.*
import java.lang.reflect.Type
import java.util.*

internal class CryptoComparePriceMultiFullDeserializer : JsonDeserializer<PriceFull> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): PriceFull {

        val type = object : TypeToken<Map<String, PriceMultiDisplay>>() {}.type
        val typeRaw = object : TypeToken<Map<String, PriceMultiRaw>>() {}.type
        val typeDisplay = object : TypeToken<Map<String, PriceDisplay>>() {}.type
        val innerTypeRaw = object : TypeToken<Map<String, PriceRaw>>() {}.type

        val rawJson = json!!.asJsonObject.get("RAW")
        val raw: Map<String, PriceMultiRaw> = Gson().fromJson(rawJson.toString(), typeRaw)

        for (entry in raw) {
            val map = HashMap<String, PriceRaw>()
            map.putAll(Gson().fromJson(rawJson.asJsonObject.get(entry.key).toString(), innerTypeRaw))

            entry.value.data = map
        }

        val displayJson = json.asJsonObject.get("DISPLAY")
        val display: Map<String, PriceMultiDisplay> = Gson().fromJson(displayJson.toString(), type)

        for (entry in display) {
            val map = HashMap<String, PriceDisplay>()
            map.putAll(Gson().fromJson(displayJson.asJsonObject.get(entry.key).toString(), typeDisplay))

            entry.value.data = map
        }

        return PriceFull(raw, display)
    }
}