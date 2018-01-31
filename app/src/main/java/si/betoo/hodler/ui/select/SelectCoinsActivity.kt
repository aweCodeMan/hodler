package si.betoo.hodler.ui.select

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
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

        setupToolbar(toolbar)

        setMainRecyclerView(recyclerView)

        presenter.onCreate()
    }

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.title = getString(R.string.select_coins)
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
        inflater.inflate(R.menu.select_coins_toolbar, menu)

        val search: SearchView = menu.findItem(R.id.action_search).actionView as SearchView
        search.queryHint = getString(R.string.search)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                presenter.onQueryChanged(newText.toString())
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.onQueryChanged(query.toString())
                return true
            }
        })

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

    override fun showNumberOfSelectedCoins(numberOfSelectedCoins: Int) {
        if (numberOfSelectedCoins == 0) {
            toolbar.subtitle = getString(R.string.no_coins_selected)
        } else {
            toolbar.subtitle = getString(R.string.selected_number_of_coins, numberOfSelectedCoins)
        }
    }
}
