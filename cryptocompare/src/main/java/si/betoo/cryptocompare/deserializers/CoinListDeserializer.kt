package si.betoo.cryptocompare.deserializers

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import si.betoo.cryptocompare.data.Coin
import si.betoo.cryptocompare.data.CoinMap
import java.lang.reflect.Type

internal class CoinListDeserializer : JsonDeserializer<CoinMap> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CoinMap {

        val type = object : TypeToken<Map<String, Coin>>() {}.type
        val data: Map<String, Coin> = Gson().fromJson(json.toString(), type)

        return CoinMap(data)
    }
}