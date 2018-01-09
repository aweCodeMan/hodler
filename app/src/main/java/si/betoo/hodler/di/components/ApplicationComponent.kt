package si.betoo.hodler.di.components

import dagger.Component
import si.betoo.cryptocompare.CryptoCompare
import si.betoo.hodler.data.coin.CoinService
import si.betoo.hodler.data.coin.HoldingService
import si.betoo.hodler.di.modules.ApplicationModule
import si.betoo.hodler.ui.base.BaseApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {
    fun inject(application: BaseApplication)

    fun coinService(): CoinService
    fun holdingService(): HoldingService
    fun cryptoCompareAPI(): CryptoCompare
}