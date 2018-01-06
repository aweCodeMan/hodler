package si.betoo.hodler.ui.select

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import kotterknife.bindView
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin

class SelectCoinsAdapter(var listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Coin> = ArrayList()

    interface OnItemClickListener {
        fun onCoinToggle(item: Coin)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_selectable_coin, parent, false)
        return ViewHolder(root)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    inner class ViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {

        private val textFullName: TextView by bindView(R.id.text_name)
        private val textSymbol: TextView by bindView(R.id.text_symbol)
        private val toggle: CheckBox by bindView(R.id.toggle)

        fun bind(coin: Coin) {
            rootView.setOnClickListener({
                onCoinToggled(coin)
                toggle.isChecked = !toggle.isChecked
            })

            toggle.setOnClickListener {
                onCoinToggled(coin)
            }

            toggle.isChecked = coin.isActive

            textFullName.text = coin.name
            textSymbol.text = coin.symbol
        }

        private fun onCoinToggled(coin: Coin) {
            coin.isActive = !coin.isActive
            listener.onCoinToggle(coin)
        }
    }


    fun setCoins(coins: List<Coin>) {
        items = coins
        notifyDataSetChanged()
    }
}