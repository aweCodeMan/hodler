package si.betoo.hodler.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import si.betoo.hodler.data.coin.CoinService
import si.betoo.hodler.data.database.Database
import si.betoo.hodler.data.api.CryptoCompareAPI
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideCoinService(): CoinService = CoinService(provideCryptoCompareAPI(), provideRoomDatabase())

    @Provides
    @Singleton
    fun provideCryptoCompareAPI(): CryptoCompareAPI = CryptoCompareAPI.create(provideApplicationContext())

    @Provides
    @Singleton
    fun provideRoomDatabase(): Database = Room.databaseBuilder(provideApplicationContext(), Database::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
}