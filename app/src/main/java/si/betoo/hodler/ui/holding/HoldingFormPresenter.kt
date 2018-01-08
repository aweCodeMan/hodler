package si.betoo.hodler.ui.holding

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import si.betoo.hodler.data.coin.Holding
import si.betoo.hodler.data.coin.HoldingService
import timber.log.Timber

class HoldingFormPresenter(private var view: HoldingFormlMVP.View, private val holdingService: HoldingService) : HoldingFormlMVP.Presenter {

    private var holding: Holding? = null

    override fun loadHolding(id: Long) {
        holdingService.find(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ holding ->

                    this.holding = holding
                    view.loadHolding(holding)
                }, { error -> Timber.e(error) })

    }

    override fun saveHolding(symbol: String, amount: Double) {
        if(holding != null)
        {
            holding!!.amount = amount
            holdingService.saveHolding(holding!!)
        }
        else
        {
            holdingService.saveHolding(Holding(symbol, amount))
        }
        view.closeView()
    }
}