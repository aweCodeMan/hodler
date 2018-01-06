package si.betoo.hodler.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.CoinService
import timber.log.Timber


class MainPresenter(private var view: MainMVP.View, private val coinService: CoinService) : MainMVP.Presenter {

    override fun onCreate() {
        coinService.getActiveCoins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coins ->
                    view.showCoins(coins)
                }, { error -> Timber.e(error) })
    }

    override fun onAddClicked() {
        view.showAddScreen()
    }
}