package si.betoo.hodler.di.modules

import android.content.Context
import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import si.betoo.hodler.data.coin.CoinService
import si.betoo.hodler.data.coin.TransactionService
import si.betoo.hodler.ui.detail.CoinDetailMVP
import si.betoo.hodler.ui.detail.CoinDetailPresenter
import si.betoo.hodler.ui.transaction.TransactionFormPresenter
import si.betoo.hodler.ui.transaction.TransactionFormMVP
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
    fun provideHoldingFormView(): TransactionFormMVP.View = activity as TransactionFormMVP.View

    @Provides
    fun provideMainPresenter(view: MainMVP.View, coinService: CoinService): MainMVP.Presenter = MainPresenter(view, coinService)

    @Provides
    fun provideSelectCoinsPresenter(view: SelectCoinsMVP.View, coinService: CoinService): SelectCoinsMVP.Presenter = SelectCoinsPresenter(view, coinService)

    @Provides
    fun provideCoinDetailPresenter(view: CoinDetailMVP.View, coinService: CoinService, transactionService: TransactionService): CoinDetailMVP.Presenter = CoinDetailPresenter(view, coinService, transactionService)

    @Provides
    fun provideHoldingFormPresenter(view: TransactionFormMVP.View, transactionService: TransactionService): TransactionFormMVP.Presenter = TransactionFormPresenter(view, transactionService)
}