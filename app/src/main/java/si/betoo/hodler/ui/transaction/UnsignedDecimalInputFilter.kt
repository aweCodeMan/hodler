package si.betoo.hodler.ui.transaction

import android.text.InputFilter
import android.text.Spanned

class UnsignedDecimalInputFilter : InputFilter {

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
        var result = ""

        for (i in start until end) {
            if (".,".contains(source[i])) {
                if (canPlaceDot(result, dest)) {
                    val combined = result + dest

                    if (combined.isEmpty()) {
                        result += "0."
                    } else {
                        result += "."
                    }
                }
            } else if (Character.isDigit(source[i])) {
                if (isZero(source[i]) && canPlaceZero(result, dest)) {
                    result += source[i]
                } else if (!isZero(source[i])) {
                    result += source[i]
                }
            }
        }

        return result
    }

    private fun canPlaceZero(result: String, dest: Spanned): Boolean {
        val combined = result + dest

        if (combined.isNotEmpty()) {
            if (!canPlaceDot(result, dest)) {
                return true
            }

            return !combined[0].equals('0')
        }

        return true
    }

    private fun isZero(c: Char): Boolean {
        val b = c.equals('0')

        return b
    }

    private fun canPlaceDot(result: String, dest: Spanned): Boolean {
        return !result.contains(".") && !dest.contains(".")
    }
}