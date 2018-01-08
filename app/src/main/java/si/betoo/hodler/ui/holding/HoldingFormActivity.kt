package si.betoo.hodler.ui.holding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.EditText
import butterknife.ButterKnife
import kotterknife.bindView
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Holding
import si.betoo.hodler.ui.base.BaseActivity
import javax.inject.Inject

class HoldingFormActivity : BaseActivity(), HoldingFormlMVP.View {


    @Inject
    lateinit var presenter: HoldingFormlMVP.Presenter

    private lateinit var symbol: String

    //  Views
    private val toolbar: Toolbar by bindView(R.id.toolbar)

    private val amount: EditText by bindView(R.id.edit_amount)
    private val submit: Button by bindView(R.id.button_submit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holding_form)

        ButterKnife.bind(this)
        graph.inject(this)

        symbol = intent.getStringExtra(HoldingFormActivity.INTENT_COIN)
        val holdingId = intent.getLongExtra(HoldingFormActivity.INTENT_HOLDING, -1)

        if (holdingId > 0) {
            presenter.loadHolding(holdingId)
        }

        setupToolbar(toolbar)

        submit.setOnClickListener { presenter.saveHolding(symbol, amount.text.toString().toDouble()) }
    }

    companion object {
        private val INTENT_COIN: String = "symbol"
        private val INTENT_HOLDING: String = "holding_id"

        fun start(context: Activity, coin: Coin, holding: Holding?) {
            val starter = Intent(context, HoldingFormActivity::class.java)
            starter.putExtra(INTENT_COIN, coin.symbol)
            starter.putExtra(INTENT_HOLDING, holding?.id)
            context.startActivity(starter)
        }
    }

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        toolbar.title = getString(R.string.add_holding)
    }

    override fun loadHolding(holding: Holding) {

        amount.setText(holding.amount.toString())
    }

    override fun closeView() {
        finish()
    }
}
