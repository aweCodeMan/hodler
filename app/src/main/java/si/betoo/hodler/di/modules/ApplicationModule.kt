package si.betoo.hodler.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import si.betoo.cryptocompare.CryptoCompare
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.CoinService
import si.betoo.hodler.data.database.Database
import si.betoo.hodler.data.coin.TransactionService
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideCoinService(): CoinService = CoinService(provideCryptoCompareAPI(), provideRoomDatabase(), provideAvailableConvertCurrencies(provideApplicationContext()))

    @Provides
    @Singleton
    fun provideHoldingService(): TransactionService = TransactionService(provideRoomDatabase())

    @Provides
    @Singleton
    fun provideCryptoCompareAPI(): CryptoCompare = CryptoCompare.create(ChuckInterceptor(provideApplicationContext()))

    @Provides
    @Singleton
    fun provideRoomDatabase(): Database = Room.databaseBuilder(provideApplicationContext(), Database::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideAvailableConvertCurrencies(context: Context): Map<String, String> {
        val codes = context.resources.getStringArray(R.array.available_total_currencies_code)
        val symbols = context.resources.getStringArray(R.array.available_total_currencies_symbol)

        val map: MutableMap<String, String> = HashMap()

        codes.forEachIndexed { index, code -> map.put(code, symbols[index]) }

        return map
    }
}