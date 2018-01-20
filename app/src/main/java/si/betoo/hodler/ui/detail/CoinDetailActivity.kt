package si.betoo.hodler.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import butterknife.ButterKnife
import kotterknife.bindView
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Transaction
import si.betoo.hodler.data.coin.Price
import si.betoo.hodler.ui.base.BaseActivity
import si.betoo.hodler.ui.transaction.TransactionFormActivity
import javax.inject.Inject

class CoinDetailActivity : BaseActivity(), CoinDetailMVP.View {



    override fun showHoldings(transactions: List<Transaction>) {
        adapter.setHoldings(transactions)
    }

    override fun showHoldingForm(coin: Coin) {
        TransactionFormActivity.start(this, coin, null )
    }

    override fun showHoldingForm(coin: Coin, transaction: Transaction) {
        TransactionFormActivity.start(this, coin, transaction)
    }

    override fun showPrices(prices: List<Price>) {
        adapter.setPrices(prices)
    }


    @Inject
    lateinit var presenter: CoinDetailMVP.Presenter

    //  Views
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)

    private lateinit var adapter: CoinHoldingsAdapter

    private lateinit var symbol: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)

        ButterKnife.bind(this)
        graph.inject(this)

        symbol = intent.getStringExtra(INTENT_COIN)
        presenter.onCreate(symbol)

        setupToolbar(toolbar)
        setRecyclerView(recyclerView)
    }

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        private val INTENT_COIN: String = "currencyCode"

        fun start(context: Activity, coin: Coin) {
            val starter = Intent(context, CoinDetailActivity::class.java)
            starter.putExtra(INTENT_COIN, coin.symbol)
            context.startActivity(starter)
        }
    }

    private fun setRecyclerView(recyclerView: RecyclerView) {

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //  Needed so it stretches the width of the cards on the initial load
        manager.isAutoMeasureEnabled = false

        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator.changeDuration = 0L

        adapter = CoinHoldingsAdapter(object : CoinHoldingsAdapter.OnItemClickListener {

            override fun onAddClicked(view: View) {
                presenter.onAddHoldingClicked()
            }

            override fun onHoldingClicked(item: Transaction) {
                presenter.onHoldingClicked(item)
            }
        })

        adapter.setHasStableIds(true)

        recyclerView.adapter = adapter
    }

    override fun showCoin(coin: Coin) {
        toolbar.title = coin.symbol
        toolbar.subtitle = coin.name
    }
}
