package si.betoo.cryptocompare

import android.content.Context
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoCompareAPI {

    @GET("data/all/coinlist")
    fun getCoinList(): Observable<CryptoCompareWrapper<CryptoCompareCoinList>>

    @GET("data/pricemultifull")
    fun getCoinPriceMultiFull(@Query("fsyms") coin: String, @Query("tsyms") currency: String): Observable<CryptoComparePriceMultiFull>

    companion object {
        fun create(interceptor: Interceptor): CryptoCompareAPI {

            val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create(GsonBuilder()
                                    .registerTypeAdapter(CryptoCompareCoinPrice::class.java, CoinPriceDeserializer())
                                    .registerTypeAdapter(CryptoCompareCoinList::class.java, CoinListDeserializer())
                                    .registerTypeAdapter(CryptoComparePriceMultiFull::class.java, CryptoComparePriceMultiFullDeserializer())
                                    .create()))
                    .baseUrl("https://min-api.cryptocompare.com/")
                    .client(client)
                    .build()

            return retrofit.create(CryptoCompareAPI::class.java)
        }
    }
}