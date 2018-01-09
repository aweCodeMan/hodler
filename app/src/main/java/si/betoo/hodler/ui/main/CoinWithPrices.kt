package si.betoo.hodler.ui.main

import si.betoo.hodler.data.coin.CoinWithHoldings
import si.betoo.hodler.data.coin.Price

data class CoinWithPrices(val coin: CoinWithHoldings, val prices: MutableMap<String, Price>)