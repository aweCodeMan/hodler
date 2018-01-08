package si.betoo.hodler.ui.detail

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.CoinService
import si.betoo.hodler.data.coin.Holding
import si.betoo.hodler.data.coin.HoldingService
import timber.log.Timber

class CoinDetailPresenter(private var view: CoinDetailMVP.View, private val coinService: CoinService, private val holdingService: HoldingService) : CoinDetailMVP.Presenter {

    private lateinit var coin: Coin

    override fun onCreate(symbol: String) {
        coinService.getCoin(symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coin ->
                    this.coin = coin
                    view.showCoin(coin)

                    loadHoldings(coin)
                    loadPrice(coin)
                }, { error -> Timber.e(error) })
    }

    override fun onAddHoldingClicked() {
        view.showHoldingForm(coin)
    }

    override fun onHoldingClicked(item: Holding) {
        view.showHoldingForm(coin, item)
    }

    private fun loadHoldings(coin: Coin) {
        holdingService.getHoldingsForCoin(coin.symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ holdings ->
                    view.showHoldings(holdings)

                    loadHoldings(coin)
                }, { error -> Timber.e(error) })
    }

    private fun loadPrice(coin: Coin) {
        coinService.getPricesForCoins(coin.symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ prices ->
                    view.showPrices(prices)

                }, { error -> Timber.e(error) })
    }
}