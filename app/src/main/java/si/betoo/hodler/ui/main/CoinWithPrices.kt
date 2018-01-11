package si.betoo.hodler.ui.main

import si.betoo.hodler.data.coin.CoinWithTransactions
import si.betoo.hodler.data.coin.Price

data class CoinWithPrices(val coin: CoinWithTransactions, val prices: MutableMap<String, Price>)