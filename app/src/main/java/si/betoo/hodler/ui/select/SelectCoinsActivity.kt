package si.betoo.hodler.ui.select

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import butterknife.ButterKnife
import kotterknife.bindView
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.ui.base.BaseActivity
import javax.inject.Inject

class SelectCoinsActivity : BaseActivity(), SelectCoinsMVP.View {

    @Inject
    lateinit var presenter: SelectCoinsMVP.Presenter

    private lateinit var adapter: SelectCoinsAdapter

    //  Views
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val progress: ProgressBar by bindView(R.id.progress)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_coins)

        ButterKnife.bind(this)
        graph.inject(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setMainRecyclerView(recyclerView)

        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    private fun setMainRecyclerView(recyclerView: RecyclerView) {

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //  Needed so it stretches the width of the cards on the initial load
        manager.isAutoMeasureEnabled = false

        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator.changeDuration = 0L

        adapter = SelectCoinsAdapter(object : SelectCoinsAdapter.OnItemClickListener {
            override fun onCoinToggle(item: Coin) {
                presenter.onCoinToggle(item)
            }
        })

        adapter.setHasStableIds(true)

        recyclerView.adapter = adapter
    }

    companion object {
        fun start(context: Activity) {
            val starter = Intent(context, SelectCoinsActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.add_toolbar, menu)
        return true
    }

    override fun showAvailableCoins(coins: List<Coin>) {
        adapter.setCoins(coins)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progress.visibility = View.VISIBLE
        } else {
            progress.visibility = View.GONE
        }
    }
}
