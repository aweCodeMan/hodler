package si.betoo.hodler.ui.main

import si.betoo.hodler.data.coin.CoinWithTransactions
import si.betoo.hodler.data.coin.Price
import java.math.BigDecimal

data class CoinWithPrices(val coin: CoinWithTransactions, val prices: MutableMap<String, Price>) {
    fun holdingsAmount(): Double {
        var amount = BigDecimal(0.0)
        coin.transactions.forEach { amount += BigDecimal(it.amount) }

        return amount.toDouble()
    }
}