package si.betoo.hodler.ui.select

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.CoinService
import timber.log.Timber

class SelectCoinsPresenter(private var view: SelectCoinsMVP.View, private val coinService: CoinService) : SelectCoinsMVP.Presenter {
    override fun onResume() {
        coinService.getAvailableCoins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coins ->
                    view.showAvailableCoins(coins)
                }, { error -> Timber.e(error) })
    }

    override fun onCreate() {

    }

    override fun onCoinToggle(item: Coin) {
        coinService.saveCoin(item)
    }
}