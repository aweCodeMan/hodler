package si.betoo.hodler.ui.select

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.CoinService
import timber.log.Timber

class SelectCoinsPresenter(private var view: SelectCoinsMVP.View, private val coinService: CoinService) : SelectCoinsMVP.Presenter {

    private var cachedAllCoins: List<Coin> = ArrayList()

    override fun onQueryChanged(query: String) {
        if (query.isNotEmpty() && cachedAllCoins.isNotEmpty()) {
            view.showAvailableCoins(cachedAllCoins.filter { it.symbol.toLowerCase().contains(query.toLowerCase()) || it.name.toLowerCase().contains(query.toLowerCase()) })
        } else if (cachedAllCoins.isNotEmpty()) {
            view.showAvailableCoins(cachedAllCoins)
        }
    }

    private var numberOfSelectedCoins = 0

    override fun onResume() {
        coinService.getAvailableCoins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coins ->
                    cachedAllCoins = coins
                    view.showAvailableCoins(coins)
                    view.showProgress(false)
                    showNumberOfSelectedCoins(coins)
                }, { error -> Timber.e(error) })
    }

    private fun showNumberOfSelectedCoins(coins: List<Coin>) {
        var number = 0

        coins.forEach { if (it.isActive) number++ }

        numberOfSelectedCoins = number
        view.showNumberOfSelectedCoins(numberOfSelectedCoins)
    }

    override fun onCreate() {

    }

    override fun onCoinToggle(item: Coin) {
        coinService.saveCoin(item)

        if (item.isActive) {
            numberOfSelectedCoins++
        } else {
            numberOfSelectedCoins--
        }

        view.showNumberOfSelectedCoins(numberOfSelectedCoins)
    }
}