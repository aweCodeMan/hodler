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

    private var coins: List<CoinWithPrices> = ArrayList()

    interface OnItemClickListener {
        fun onCoinClicked(item: Coin, view: View)
        fun onAddClicked(view: View)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddCoinViewHolder) {
            holder.bind()
        } else if (holder is ViewHolder) {
            holder.bind(coins[position])
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
        if (position < coins.size) {
            return 0
        }

        return 1
    }

    override fun getItemCount(): Int = coins.size + 1

    override fun getItemId(position: Int): Long = position.toLong()

    inner class AddCoinViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind() {
            rootView.setOnClickListener({ listener.onAddClicked(rootView) })
        }
    }

    inner class ViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val textSymbol: TextView by bindView(R.id.text_symbol)
        private val layoutPrice: ViewGroup by bindView(R.id.layout_price)

        fun bind(coinWithPrice: CoinWithPrices) {
            rootView.setOnClickListener({ listener.onCoinClicked(coinWithPrice.coin, rootView) })

            textSymbol.text = coinWithPrice.coin.symbol

            layoutPrice.removeAllViews()

            if (coinWithPrice.prices.isNotEmpty()) {
                coinWithPrice.prices.forEach {
                    if (it.value.currency.toLowerCase() != coinWithPrice.coin.symbol.toLowerCase()) {
                        val textView = TextView(layoutPrice.context)
                        textView.text = it.value.currency + ": " + it.value.price + " (" + "%.2f".format(it.value.change24HourPercent) + ")"
                        layoutPrice.addView(textView)
                    }
                }
            }

        }
    }

    fun setCoins(coins: List<CoinWithPrices>) {
        this.coins = coins
        notifyDataSetChanged()
    }

    fun updatePrices(updatedCoins: List<CoinWithPrices>) {
        updatedCoins.forEach {
            for (coin in coins) {
                if (coin.coin == it.coin) {
                    coin.prices.clear()
                    coin.prices.putAll(it.prices)

                    break
                }
            }
        }

        notifyDataSetChanged()
    }
}