package si.betoo.cryptocompare

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

internal class CryptoComparePriceMultiFullDeserializer : JsonDeserializer<CryptoComparePriceMultiFull> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CryptoComparePriceMultiFull {

        val type = object : TypeToken<Map<String, CryptoComparePriceMulti>>() {}.type
        val typeInner = object : TypeToken<Map<String, CryptoComparePrice>>() {}.type

        val rawJson = json!!.asJsonObject.get("RAW")
        val raw: Map<String, CryptoComparePriceMulti> = Gson().fromJson(rawJson.toString(), type)

        for (entry in raw) {
            val map = HashMap<String, CryptoComparePrice>()
            map.putAll(Gson().fromJson(rawJson.asJsonObject.get(entry.key).toString(), typeInner))

            entry.value.data = map
        }

        val displayJson = json.asJsonObject.get("DISPLAY")
        val display: Map<String, CryptoComparePriceMulti> = Gson().fromJson(displayJson.toString(), type)

        for (entry in display) {
            val map = HashMap<String, CryptoComparePrice>()
            map.putAll(Gson().fromJson(displayJson.asJsonObject.get(entry.key).toString(), typeInner))

            entry.value.data = map
        }

        return CryptoComparePriceMultiFull(raw, display)
    }
}