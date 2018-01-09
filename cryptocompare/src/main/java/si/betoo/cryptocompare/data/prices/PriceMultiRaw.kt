package si.betoo.cryptocompare.data.prices

import si.betoo.cryptocompare.data.prices.PriceRaw

data class PriceMultiRaw(var data: Map<String, PriceRaw>)