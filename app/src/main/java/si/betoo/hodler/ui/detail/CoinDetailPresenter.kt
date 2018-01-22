package si.betoo.hodler.ui.detail

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.CoinService
import si.betoo.hodler.data.coin.Transaction
import si.betoo.hodler.data.coin.TransactionService
import timber.log.Timber

class CoinDetailPresenter(private var view: CoinDetailMVP.View, private val coinService: CoinService, private val transactionService: TransactionService) : CoinDetailMVP.Presenter {

    private lateinit var coin: Coin
    private lateinit var symbol: String

    private var index = 0


    override fun onCreate(symbol: String) {
        this.symbol = symbol

        coinService.getCoin(symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coin ->
                    this.coin = coin
                    view.showCoin(coin)

                    loadHoldings(coin)
                }, { error -> Timber.e(error) })

        loadGraph(symbol)
    }

    private fun loadGraph(symbol: String) {

        val currencies = coinService.availableCurrencies.filter { it.key !== symbol }

        if (currencies.isNotEmpty()) {
            index++

            if (index >= currencies.size) {
                index = 0
            }

            val toSymbol = currencies.keys.elementAt(index)

            coinService.getHistoryDay(symbol, toSymbol, 30)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ history ->
                        view.showHistory(PriceHistory(symbol + "/" + toSymbol, history))
                    }, { error -> Timber.e(error) })
        }
    }

    override fun onAddHoldingClicked() {
        view.showHoldingForm(coin)
    }

    override fun onHoldingClicked(item: Transaction) {
        view.showHoldingForm(coin, item)
    }

    private fun loadHoldings(coin: Coin) {
        transactionService.getTransactionsForCoin(coin.symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ holdings ->
                    view.showHoldings(holdings)

                    loadHoldings(coin)
                }, { error -> Timber.e(error) })
    }

    override fun onChartClicked() {
        loadGraph(symbol)
    }
}