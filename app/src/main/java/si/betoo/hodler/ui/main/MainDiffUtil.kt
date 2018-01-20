package si.betoo.hodler.ui.main

import android.support.v7.util.DiffUtil


internal class MainDiffUtil(private val newList: List<CoinWithPrices>, private val oldList: List<CoinWithPrices>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].coin.coin == newList[newItemPosition].coin.coin
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].prices.equals(newList[newItemPosition].prices)
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size
}