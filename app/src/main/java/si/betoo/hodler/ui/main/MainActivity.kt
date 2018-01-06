package si.betoo.hodler.ui.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife
import kotterknife.bindView
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.ui.select.SelectCoinsActivity
import si.betoo.hodler.ui.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity(), MainMVP.View {


    @Inject
    lateinit var presenter: MainMVP.Presenter

    private lateinit var adapter: MainAdapter

    //  Views
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)

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

            override fun onCoinClicked(item: String, view: View) {
            }
        })

        adapter.setHasStableIds(true)

        recyclerView.adapter = adapter
    }

    override fun showCoins(coins: List<Coin>) {
        adapter.setCoins(coins)
    }

    override fun showAddScreen() {
        SelectCoinsActivity.start(this)
    }
}
