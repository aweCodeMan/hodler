package si.betoo.hodler.ui.main

import si.betoo.hodler.data.coin.CoinWithTransactions
import si.betoo.hodler.data.coin.Price
import si.betoo.hodler.data.coin.Transaction
import java.math.BigDecimal

data class CoinWithPrices(val coin: CoinWithTransactions, val prices: MutableMap<String, Price>) {
    fun holdingsAmount(): Double {
        var amount = BigDecimal(0.0)

        coin.transactions.filter { it.type == Transaction.TYPE_BUY }.forEach { amount += BigDecimal(it.amount) }

        return amount.toDouble()
    }
}