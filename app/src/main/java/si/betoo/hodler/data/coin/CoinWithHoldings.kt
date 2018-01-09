package si.betoo.hodler.data.coin

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class CoinWithHoldings(@Embedded var coin: Coin,
                       @Relation(parentColumn = "symbol", entityColumn = "symbol", entity = Holding::class) var holdings: List<Holding>) {

    constructor() : this(Coin("N/A", "", -1, false), ArrayList())
}
