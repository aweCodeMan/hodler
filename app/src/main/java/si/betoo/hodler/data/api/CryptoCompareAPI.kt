package si.betoo.hodler.data.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import si.betoo.hodler.data.cryptocompare.CryptoCompareCoinList
import si.betoo.hodler.data.cryptocompare.CryptoCompareWrapper

interface CryptoCompareAPI {

    @GET("data/all/coinlist")
    fun getCoinList(): Observable<CryptoCompareWrapper<CryptoCompareCoinList>>

    companion object {
        fun create(context: Context): CryptoCompareAPI {

            val client = OkHttpClient.Builder()
                    .addInterceptor(ChuckInterceptor(context))
                    .build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create(GsonBuilder()
                                    .registerTypeAdapter(CryptoCompareCoinList::class.java, CoinListDeserializer())
                                    .create()))
                    .baseUrl("https://min-api.cryptocompare.com/")
                    .client(client)
                    .build()

            return retrofit.create(CryptoCompareAPI::class.java)
        }
    }
}