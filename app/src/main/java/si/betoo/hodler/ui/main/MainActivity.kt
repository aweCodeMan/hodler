package si.betoo.hodler.ui.main

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.ButterKnife
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.activity_main.*
import kotterknife.bindView
import si.betoo.hodler.CurrencyFormatter
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.roundTo2DecimalPlaces
import si.betoo.hodler.ui.select.SelectCoinsActivity
import si.betoo.hodler.ui.base.BaseActivity
import si.betoo.hodler.ui.detail.CoinDetailActivity
import si.betoo.hodler.ui.settings.SettingsActivity
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainMVP.View {

    @Inject
    lateinit var presenter: MainMVP.Presenter

    private lateinit var adapter: MainAdapter

    //  Views
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private val appBarLayout: AppBarLayout by bindView(R.id.appbar)
    private val collapsingToolbarLayout: CollapsingToolbarLayout by bindView(R.id.collapse_toolbar_layout)

    private val layoutTotal: ViewGroup by bindView(R.id.layout_total)
    private val toolbar: Toolbar by bindView(R.id.toolbar)

    private val progress: ProgressBar by bindView(R.id.progress)
    private val progressTotal: ProgressBar by bindView(R.id.progress_total)

    private val textHoldingValue: TextView by bindView(R.id.text_holding_value)
    private val textPercentChange: TextView by bindView(R.id.text_percent_change)
    private val textTotalChange: TextView by bindView(R.id.text_total_change)

    private val toolbarText: TextView by bindView(R.id.toolbar_title)

    private val buttonRefresh: FloatingActionButton by bindView(R.id.button_refresh)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        graph.inject(this)
        presenter.onCreate()
        setMainRecyclerView(recyclerView)

        setToolbarListeners(toolbar)

        buttonRefresh.setOnClickListener({
            presenter.refreshPrices()
        })

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {

            var isExpanded = true

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                val before = isExpanded

                isExpanded = Math.abs(verticalOffset) <= toolbar.height

                toolbar.post {
                    if (before != isExpanded) {
                        if (!isExpanded) {
                            YoYo.with(Techniques.FadeIn)
                                    .duration(200)
                                    .playOn(toolbarText)

                            toolbarText.visibility = View.VISIBLE
                            layoutTotal.visibility = View.INVISIBLE

                        } else {
                            YoYo.with(Techniques.FadeIn)
                                    .duration(200)
                                    .playOn(layoutTotal)

                            layoutTotal.visibility = View.VISIBLE
                            toolbarText.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        })
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
            progressTotal.visibility = View.VISIBLE

            layoutTotal.visibility = View.GONE

            buttonRefresh.visibility = View.GONE
        } else {
            progress.visibility = View.GONE
            progressTotal.visibility = View.GONE

            layoutTotal.visibility = View.VISIBLE

            buttonRefresh.visibility = View.VISIBLE
        }
    }

    override fun updatePrices(prices: List<CoinWithPrices>, currencyCode: String) {
        adapter.updatePrices(prices, currencyCode)
    }

    override fun showTotal(total: Double, currencyCode: String, currencySymbol: String) {
        val formatter = CurrencyFormatter(currencyCode, currencySymbol)

        textHoldingValue.text = formatter.format(total)
        toolbarText.text = formatter.format(total)
    }

    override fun showPercentChange(percentChange: Double) {
        textPercentChange.text = "" + percentChange.roundTo2DecimalPlaces() + "%"
    }

    override fun showTotalChange(totalChange: Double) {
        textTotalChange.text = "" + totalChange.roundTo2DecimalPlaces()
    }

    override fun showCoinDetail(coin: Coin) {
        CoinDetailActivity.start(this, coin)
    }

    private fun setToolbarListeners(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        toolbar.setOnClickListener({
            presenter.switchCurrentCurrency()
        })
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

    override fun showSettings() {
        SettingsActivity.start(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_toolbar, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                presenter.onSettingsClicked()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }
}
