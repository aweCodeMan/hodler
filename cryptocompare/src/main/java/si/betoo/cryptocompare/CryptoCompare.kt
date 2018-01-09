package si.betoo.cryptocompare

import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import si.betoo.cryptocompare.data.CoinMap
import si.betoo.cryptocompare.data.prices.PriceMap
import si.betoo.cryptocompare.data.prices.PriceFull
import si.betoo.cryptocompare.data.Wrapper
import si.betoo.cryptocompare.deserializers.CoinListDeserializer
import si.betoo.cryptocompare.deserializers.PriceDeserializer
import si.betoo.cryptocompare.deserializers.CryptoComparePriceMultiFullDeserializer

interface CryptoCompare {

    @GET("data/all/coinlist")
    fun getCoinList(): Observable<Wrapper<CoinMap>>

    @GET("data/pricemultifull")
    fun getCoinPriceMultiFull(@Query("fsyms") coin: String, @Query("tsyms") currency: String): Observable<PriceFull>

    companion object {
        fun create(interceptor: Interceptor): CryptoCompare {

            val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create(GsonBuilder()
                                    .registerTypeAdapter(PriceMap::class.java, PriceDeserializer())
                                    .registerTypeAdapter(CoinMap::class.java, CoinListDeserializer())
                                    .registerTypeAdapter(PriceFull::class.java, CryptoComparePriceMultiFullDeserializer())
                                    .create()))
                    .baseUrl("https://min-api.cryptocompare.com/")
                    .client(client)
                    .build()

            return retrofit.create(CryptoCompare::class.java)
        }
    }
}