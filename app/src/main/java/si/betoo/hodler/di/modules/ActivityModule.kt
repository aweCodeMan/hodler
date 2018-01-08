package si.betoo.hodler.di.modules

import android.content.Context
import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import si.betoo.hodler.data.coin.CoinService
import si.betoo.hodler.data.coin.HoldingService
import si.betoo.hodler.ui.detail.CoinDetailMVP
import si.betoo.hodler.ui.detail.CoinDetailPresenter
import si.betoo.hodler.ui.holding.HoldingFormPresenter
import si.betoo.hodler.ui.holding.HoldingFormlMVP
import si.betoo.hodler.ui.select.SelectCoinsMVP
import si.betoo.hodler.ui.select.SelectCoinsPresenter
import si.betoo.hodler.ui.main.MainMVP
import si.betoo.hodler.ui.main.MainPresenter

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    fun provideActivityContext(): Context = activity

    @Provides
    fun provideMainView(): MainMVP.View = activity as MainMVP.View

    @Provides
    fun provideAddView(): SelectCoinsMVP.View = activity as SelectCoinsMVP.View

    @Provides
    fun provideCoinDetailView(): CoinDetailMVP.View = activity as CoinDetailMVP.View

    @Provides
    fun provideHoldingFormView(): HoldingFormlMVP.View = activity as HoldingFormlMVP.View

    @Provides
    fun provideMainPresenter(view: MainMVP.View, coinService: CoinService, holdingService: HoldingService): MainMVP.Presenter = MainPresenter(view, coinService, holdingService)

    @Provides
    fun provideSelectCoinsPresenter(view: SelectCoinsMVP.View, coinService: CoinService): SelectCoinsMVP.Presenter = SelectCoinsPresenter(view, coinService)

    @Provides
    fun provideCoinDetailPresenter(view: CoinDetailMVP.View, coinService: CoinService, holdingService: HoldingService): CoinDetailMVP.Presenter = CoinDetailPresenter(view, coinService, holdingService)

    @Provides
    fun provideHoldingFormPresenter(view: HoldingFormlMVP.View, holdingService: HoldingService): HoldingFormlMVP.Presenter = HoldingFormPresenter(view, holdingService)
}