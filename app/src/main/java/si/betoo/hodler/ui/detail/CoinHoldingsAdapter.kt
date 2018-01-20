package si.betoo.hodler.ui.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotterknife.bindView
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Transaction
import si.betoo.hodler.data.coin.Price

class CoinHoldingsAdapter(var listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactions: List<Transaction> = ArrayList()
    private var prices: List<Price> = ArrayList()

    interface OnItemClickListener {
        fun onHoldingClicked(item: Transaction)
        fun onAddClicked(view: View)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddHoldingViewHolder) {
            holder.bind()
        } else if (holder is HoldingViewHolder) {
            holder.bind(transactions[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val root = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
                HoldingViewHolder(root)
            }
            else -> {
                val root = LayoutInflater.from(parent.context).inflate(R.layout.item_add_transaction, parent, false)
                AddHoldingViewHolder(root)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < transactions.size) {
            return 0
        }

        return 1
    }

    override fun getItemCount(): Int = transactions.size + 1

    override fun getItemId(position: Int): Long = position.toLong()

    inner class AddHoldingViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind() {
            rootView.setOnClickListener({ listener.onAddClicked(rootView) })
        }
    }

    inner class HoldingViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val textAmount: TextView by bindView(R.id.text_amount)
        private val layoutPrice: ViewGroup by bindView(R.id.layout_price)

        fun bind(transaction: Transaction) {
            textAmount.text = transaction.amount.toString()
            rootView.setOnClickListener({ listener.onHoldingClicked(transaction) })

          /*  layoutPrice.removeAllViews()

            if (prices.isNotEmpty()) {
                prices.forEach {
                    if (it.currency.toLowerCase() !== transaction.currencyCode.toLowerCase()) {
                        val textView = TextView(layoutPrice.context)
                        textView.text = it.currency + ": " + (it.price * transaction.amount)
                        layoutPrice.addView(textView)
                    }
                }
            }*/
        }
    }

    fun setHoldings(coins: List<Transaction>) {
        this.transactions = coins
        notifyDataSetChanged()
    }

    fun setPrices(prices: List<Price>) {
        this.prices = prices
        notifyDataSetChanged()
    }
}