package si.betoo.cryptocompare.deserializers

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import si.betoo.cryptocompare.data.AllExchanges
import si.betoo.cryptocompare.data.Exchange
import java.lang.reflect.Type

internal class ExchangeDeserializer : JsonDeserializer<Exchange> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Exchange {

        val type = object : TypeToken<List<String>>() {}.type
        val data: List<String> = Gson().fromJson(json.toString(), type)

        return Exchange(data)
    }
}