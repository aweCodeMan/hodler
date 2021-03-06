package si.betoo.hodler.ui.main

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotterknife.bindView
import si.betoo.hodler.CurrencyFormatter
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.roundTo2DecimalPlaces

class MainAdapter(var listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var coins: List<CoinWithPrices> = ArrayList()
    private var currentCurrencyCode: String? = null

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
        private val textAmount: TextView by bindView(R.id.text_amount)
        private val textAmountValue: TextView by bindView(R.id.text_amount_value)

        private val coinPrice: CoinPricesCompoundView by bindView(R.id.price)

        fun bind(coinWithPrice: CoinWithPrices) {
            rootView.setOnClickListener({ listener.onCoinClicked(coinWithPrice.coin.coin, rootView) })

            val amount = coinWithPrice.holdingsAmount()

            if (amount == 0.0) {
                textAmount.visibility = View.INVISIBLE
                textAmountValue.visibility = View.INVISIBLE
            } else {
                textAmount.visibility = View.VISIBLE
                textAmountValue.visibility = View.VISIBLE
            }

            textAmount.text = amount.toString()

            textSymbol.text = coinWithPrice.coin.coin.symbol

            textAmountValue.text = ""

            val price = coinWithPrice.prices.filter { it.value.currency.toLowerCase() != coinWithPrice.coin.coin.symbol.toLowerCase() }

            currentCurrencyCode.let {

                for (entry in price) {
                    if (entry.key == it) {
                        coinPrice.visibility = View.VISIBLE
                        coinPrice.showPrice(entry)

                        //  Also use price to show value for the amount of coins
                        //textAmountValue.text = entry.value.currencySymbol + (entry.value.price * amount).roundTo2DecimalPlaces()

                        val formatter = CurrencyFormatter(entry.value.currency, entry.value.currencySymbol)
                        textAmountValue.text = formatter.format((entry.value.price * amount).roundTo2DecimalPlaces())
                        break
                    } else {
                        coinPrice.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    fun setCoins(newCoins: List<CoinWithPrices>) {
        val diffResults = DiffUtil.calculateDiff(MainDiffUtil(newCoins, this.coins), false)
        this.coins = newCoins
        diffResults.dispatchUpdatesTo(this)
    }

    fun updatePrices(updatedCoins: List<CoinWithPrices>, currencyCode: String) {
        currentCurrencyCode = currencyCode

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