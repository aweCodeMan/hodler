package si.betoo.hodler.ui.detail

import si.betoo.cryptocompare.data.History

data class PriceHistory(val pair: String, val history: List<History>)