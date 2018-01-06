package si.betoo.hodler.ui.main

import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Price

data class CoinWithPrices(val coin: Coin, val prices: MutableMap<String, Price>)