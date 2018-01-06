package si.betoo.hodler.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotterknife.bindView
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin


class MainAdapter(var listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Coin> = ArrayList()

    interface OnItemClickListener {
        fun onCoinClicked(item: String, view: View)
        fun onAddClicked(view: View)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddCoinViewHolder) {
            holder.bind()
        } else if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val root = LayoutInflater.from(parent.context).inflate(R.layout.item_coin, parent, false)
                ViewHolder(root)
            }
            else -> {
                val root = LayoutInflater.from(parent.context).inflate(R.layout.item_select_coins, parent, false)
                AddCoinViewHolder(root)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < items.size) {
            return 0
        }

        return 1
    }

    override fun getItemCount(): Int = items.size + 1

    override fun getItemId(position: Int): Long = position.toLong()

    inner class AddCoinViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind() {
            rootView.setOnClickListener({ listener.onAddClicked(rootView) })
        }
    }

    inner class ViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val textSymbol: TextView by bindView(R.id.text_symbol)

        fun bind(coin: Coin) {
            textSymbol.text = coin.symbol
        }
    }

    fun setCoins(coins: List<Coin>) {
        items = coins
        notifyDataSetChanged()
    }
}