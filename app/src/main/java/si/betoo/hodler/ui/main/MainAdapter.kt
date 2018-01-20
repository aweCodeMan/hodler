package si.betoo.hodler.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotterknife.bindView
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.roundTo2DecimalPlaces
import java.math.BigDecimal

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

        private val layoutPrice: ViewGroup by bindView(R.id.layout_price)

        fun bind(coinWithPrice: CoinWithPrices) {
            rootView.setOnClickListener({ listener.onCoinClicked(coinWithPrice.coin.coin, rootView) })

            textSymbol.text = coinWithPrice.coin.coin.symbol

            val amount = coinWithPrice.holdingsAmount()

            textAmount.text = amount.toString()

            layoutPrice.removeAllViews()

            textAmountValue.text = ""

            if (coinWithPrice.prices.isNotEmpty()) {
                coinWithPrice.prices.forEach {
                    val price = it

                    if (price.value.currency.toLowerCase() != coinWithPrice.coin.coin.symbol.toLowerCase()) {

                        currentCurrencyCode.let {

                            if (price.key == it) {
                                val view = CoinPricesCompoundView(layoutPrice.context)
                                view.showPrice(price)
                                layoutPrice.addView(view)

                                //  Also use price to show value for the amount of coins
                                textAmountValue.text = price.value.currencySymbol + (price.value.price * amount).roundTo2DecimalPlaces()
                            }
                        }
                    }
                }
            }

        }
    }

    fun setCoins(coins: List<CoinWithPrices>) {
        this.coins = coins
        notifyDataSetChanged()
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