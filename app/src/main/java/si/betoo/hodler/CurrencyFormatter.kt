package si.betoo.hodler

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

class CurrencyFormatter(val currencyCode: String, val currencySymbol: String) {
    fun format(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance(currencyCode)

        val decimalFormatSymbols = DecimalFormatSymbols()
        decimalFormatSymbols.setCurrencySymbol(currencySymbol)
        (formatter as DecimalFormat).setDecimalFormatSymbols(decimalFormatSymbols)

        return formatter.format(amount)
    }
}