package si.betoo.hodler.data.api

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import si.betoo.hodler.data.cryptocompare.CryptoCompareCoin
import si.betoo.hodler.data.cryptocompare.CryptoCompareCoinList
import java.lang.reflect.Type

internal class CoinListDeserializer : JsonDeserializer<CryptoCompareCoinList> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CryptoCompareCoinList {

        val type = object : TypeToken<Map<String, CryptoCompareCoin>>() {}.type
        val data: Map<String, CryptoCompareCoin> = Gson().fromJson(json.toString(), type)

        return CryptoCompareCoinList(data)
    }
}