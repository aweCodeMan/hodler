package si.betoo.hodler.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.ButterKnife
import kotterknife.bindView
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Price
import si.betoo.hodler.roundTo2DecimalPlaces

class CoinPricesCompoundView : LinearLayout {

    private val textPrice: TextView by bindView(R.id.text_price)
    private val textChange: TextView by bindView(R.id.text_change)


    constructor(context: Context) : this(context, null) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        orientation = LinearLayout.HORIZONTAL

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.compound_coin_prices, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        ButterKnife.bind(this)
    }

    fun showPrice(price: Map.Entry<String, Price>) {
        textPrice.text = price.value.currencySymbol + price.value.price.toString()

        val change = price.value.change24HourPercent.roundTo2DecimalPlaces()

        if (change >= 0.0) {
            textChange.setTextColor(resources.getColor(R.color.colorPositive))
        } else {
            textChange.setTextColor(resources.getColor(R.color.colorNegative))
        }

        textChange.text = context.getString(R.string.change_daily, change.toString())
    }
}