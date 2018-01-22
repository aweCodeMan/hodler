package si.betoo.hodler.ui.detail

import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import kotterknife.bindView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import si.betoo.cryptocompare.data.History
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Transaction

class CoinHoldingsAdapter(var listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactions: List<Transaction> = ArrayList()
    private var history: PriceHistory = PriceHistory("", ArrayList())

    interface OnItemClickListener {
        fun onHoldingClicked(item: Transaction)
        fun onChartClicked()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChartViewHolder) {
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
                val root = LayoutInflater.from(parent.context).inflate(R.layout.item_chart, parent, false)
                ChartViewHolder(root)
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

    inner class ChartViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {

        private val textSymbol: TextView by bindView(R.id.text_symbol)
        private val chart: CandleStickChart by bindView(R.id.chart)

        fun bind() {
            if (history.history.isNotEmpty()) {
                textSymbol.text = history.pair

                val list: MutableList<CandleEntry> = ArrayList(history.history.size)

                for (hist in history.history) {
                    val candle = CandleEntry(hist.time.toFloat(), hist.high.toFloat(), hist.low.toFloat(), hist.open.toFloat(), hist.close.toFloat())
                    list.add(candle)
                }

                chart.data = CandleMaker.makeCandleData(history.history, "day")
                chart.setBackgroundColor(Color.WHITE)
                chart.setTouchEnabled(false)

                val xAxis = chart.xAxis
                setupXAxis(xAxis, history.history)
                chart.legend.isEnabled = false

                chart.invalidate()

                rootView.setOnClickListener({ listener.onChartClicked() })

            }
        }
    }

    inner class HoldingViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val textAmount: TextView by bindView(R.id.text_amount)
        private val textType: TextView by bindView(R.id.text_type)

        fun bind(transaction: Transaction) {
            textAmount.text = transaction.amount.toString()

            textType.text = if (transaction.type == Transaction.TYPE_BUY) "Buy" else "Sell"
            rootView.setOnClickListener({ listener.onHoldingClicked(transaction) })
        }
    }

    fun setHoldings(coins: List<Transaction>) {
        this.transactions = coins
        notifyDataSetChanged()
    }

    fun showHistory(history: PriceHistory) {
        this.history = history
        notifyDataSetChanged()
    }

    private fun setupXAxis(xAxis: XAxis, history: List<History>) {
        xAxis.setValueFormatter({ value: Float, axisBase: AxisBase -> DateTime(history[value.toInt()].time * 1000).toLocalDateTime().toString(DateTimeFormat.shortDateTime()) })
        xAxis.labelCount = 3
        xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

    class CandleMaker {
        companion object {
            fun makeCandleData(histoList: List<History>, period: String): CandleData {
                val candleList: ArrayList<CandleEntry> = ArrayList()
                var x = 0f

                histoList.forEach {
                    candleList.add(CandleEntry(x++, it.high.toFloat(), it.low.toFloat(), it.open.toFloat(), it.close.toFloat()))
                }

                val dataSet = CandleDataSet(candleList.toList(), period)
                setupDataSetParams(dataSet)
                return CandleData(dataSet)
            }

            private fun setupDataSetParams(dataSet: CandleDataSet) {
                dataSet.shadowColor = Color.RED
                dataSet.shadowWidth = 0.7f
                dataSet.decreasingColor = Color.RED
                dataSet.decreasingPaintStyle = Paint.Style.FILL
                dataSet.increasingColor = Color.GREEN
                dataSet.increasingPaintStyle = Paint.Style.FILL
                dataSet.neutralColor = Color.GRAY
                dataSet.setDrawValues(false)
            }
        }
    }
}