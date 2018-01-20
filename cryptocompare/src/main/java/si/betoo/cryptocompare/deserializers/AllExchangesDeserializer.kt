package si.betoo.cryptocompare.deserializers

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import si.betoo.cryptocompare.data.AllExchanges
import si.betoo.cryptocompare.data.Coin
import si.betoo.cryptocompare.data.CoinMap
import si.betoo.cryptocompare.data.Exchange
import java.lang.reflect.Type

internal class AllExchangesDeserializer : JsonDeserializer<AllExchanges> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): AllExchanges {

        val type = object : TypeToken<Map<String, Map<String, List<String>>>>() {}.type
        val data: Map<String, Map<String, List<String>>> = Gson().fromJson(json.toString(), type)

        return AllExchanges(data)
    }
}