package si.betoo.hodler.ui.main

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.ButterKnife
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotterknife.bindView
import si.betoo.hodler.CurrencyFormatter
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.roundTo2DecimalPlaces
import si.betoo.hodler.ui.select.SelectCoinsActivity
import si.betoo.hodler.ui.base.BaseActivity
import si.betoo.hodler.ui.detail.CoinDetailActivity
import si.betoo.hodler.ui.settings.SettingsActivity
import javax.inject.Inject

class MainActivity : BaseActivity(), MainMVP.View {

    @Inject
    lateinit var presenter: MainMVP.Presenter

    private lateinit var adapter: MainAdapter

    //  Views
    private val appBarLayout: AppBarLayout by bindView(R.id.app_bar_layout)

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val toolbarLayout: ViewGroup by bindView(R.id.layout_toolbar)
    private val textToolbarSubtitle: TextView by bindView(R.id.toolbar_subtitle)

    private val collapsingToolbarLayout: ViewGroup by bindView(R.id.collapse_toolbar_layout)
    private val collapsingToolbarTotal: TextView by bindView(R.id.collapse_toolbar_total)
    private val collapsingToolbarChangePercent: TextView by bindView(R.id.collapse_toolbar_change_percent)
    private val collapsingToolbarTotalChange: TextView by bindView(R.id.collapse_toolbar_total_change)

    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)

    private val progress: ProgressBar by bindView(R.id.progress)

    private val buttonRefresh: FloatingActionButton by bindView(R.id.refresh)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        graph.inject(this)

        presenter.onCreate()

        setClickListeners()
        setToolbar(toolbar)
        setRecyclerView(recyclerView)
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

    override fun showCoins(coins: List<CoinWithPrices>) {
        adapter.setCoins(coins)
    }

    override fun showAddCoinsScreen() {
        SelectCoinsActivity.start(this)
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progress.visibility = View.VISIBLE
            buttonRefresh.visibility = View.GONE
        } else {
            progress.visibility = View.GONE
            buttonRefresh.visibility = View.VISIBLE
        }
    }

    override fun updatePrices(prices: List<CoinWithPrices>, currencyCode: String) {
        adapter.updatePrices(prices, currencyCode)
    }

    override fun showTotal(total: Double, currencyCode: String, currencySymbol: String) {
        val formatter = CurrencyFormatter(currencyCode, currencySymbol)

        collapsingToolbarTotal.text = formatter.format(total.roundTo2DecimalPlaces())
        textToolbarSubtitle.text = formatter.format(total.roundTo2DecimalPlaces())
    }

    override fun showTotalChange(totalChange: Double, currencyCode: String, currencySymbol: String) {
        val formatter = CurrencyFormatter(currencyCode, currencySymbol)

        collapsingToolbarTotalChange.text = formatter.format(totalChange.roundTo2DecimalPlaces())
    }

    override fun showPercentChange(percentChange: Double) {
        collapsingToolbarChangePercent.text = getString(R.string.percent_change, percentChange.roundTo2DecimalPlaces().toString())
    }

    override fun showCoinDetailScreen(coin: Coin) {
        CoinDetailActivity.start(this, coin)
    }

    override fun showSettingsScreen() {
        SettingsActivity.start(this)
    }

    private fun setRecyclerView(recyclerView: RecyclerView) {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //  Needed so it stretches the width of the cards on the initial load
        manager.isAutoMeasureEnabled = false

        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator.changeDuration = 0L

        adapter = MainAdapter(object : MainAdapter.OnItemClickListener {
            override fun onAddClicked(view: View) {
                presenter.onAddCoinsClicked()
            }

            override fun onCoinClicked(item: Coin, view: View) {
                presenter.onCoinClicked(item)
            }
        })

        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }

    private fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isExpanded = true

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                val before = isExpanded

                isExpanded = Math.abs(verticalOffset) <= toolbar.height

                //  Need to do a post, otherwise we get an error that we tried to modify the toolbar while it was being animated/invalidated
                toolbar.post {
                    if (before != isExpanded) {
                        if (!isExpanded) {
                            toolbarLayout.visibility = View.VISIBLE
                            collapsingToolbarLayout.visibility = View.INVISIBLE

                            fadeIn(toolbarLayout)
                        } else {
                            toolbarLayout.visibility = View.INVISIBLE
                            collapsingToolbarLayout.visibility = View.VISIBLE

                            fadeIn(collapsingToolbarLayout)
                        }
                    }
                }
            }
        })
    }

    private fun setClickListeners() {
        buttonRefresh.setOnClickListener { presenter.refreshPrices() }

        collapsingToolbarLayout.setOnClickListener { presenter.switchCurrentCurrency() }
        toolbarLayout.setOnClickListener { presenter.switchCurrentCurrency() }
    }

    private fun fadeIn(view: View) {
        YoYo.with(Techniques.FadeIn)
                .duration(200)
                .playOn(view)
    }
}
