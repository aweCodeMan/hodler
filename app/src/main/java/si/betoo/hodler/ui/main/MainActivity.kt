package si.betoo.hodler.ui.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import butterknife.ButterKnife
import kotterknife.bindView
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Price
import si.betoo.hodler.ui.select.SelectCoinsActivity
import si.betoo.hodler.ui.base.BaseActivity
import si.betoo.hodler.ui.detail.CoinDetailActivity
import javax.inject.Inject

class MainActivity : BaseActivity(), MainMVP.View {
    override fun showCoinDetail(coin: Coin) {
        CoinDetailActivity.start(this, coin)
    }


    @Inject
    lateinit var presenter: MainMVP.Presenter

    private lateinit var adapter: MainAdapter

    //  Views
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private val progress: ProgressBar by bindView(R.id.progress)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        graph.inject(this)
        presenter.onCreate()
        setMainRecyclerView(recyclerView)
    }

    private fun setMainRecyclerView(recyclerView: RecyclerView) {

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //  Needed so it stretches the width of the cards on the initial load
        manager.isAutoMeasureEnabled = false

        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator.changeDuration = 0L

        adapter = MainAdapter(object : MainAdapter.OnItemClickListener {

            override fun onAddClicked(view: View) {
                presenter.onAddClicked()
            }

            override fun onCoinClicked(item: Coin, view: View) {
                presenter.onCoinClicked(item)
            }
        })

        adapter.setHasStableIds(true)

        recyclerView.adapter = adapter
    }

    override fun showCoins(coins: List<CoinWithPrices>) {
        adapter.setCoins(coins)
    }

    override fun showAddScreen() {
        SelectCoinsActivity.start(this)
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progress.visibility = View.VISIBLE
        } else {
            progress.visibility = View.GONE
        }
    }

    override fun updatePrices(prices: List<CoinWithPrices>) {
        adapter.updatePrices(prices)
    }
}
