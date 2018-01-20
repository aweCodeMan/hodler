package si.betoo.hodler.data.coin

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class CoinWithTransactions(@Embedded var coin: Coin,
                           @Relation(parentColumn = "currency_code", entityColumn = "currency_code", entity = Transaction::class) var transactions: List<Transaction>) {

    constructor() : this(Coin("N/A", "", -1, false), ArrayList())
}
