package si.betoo.hodler

import android.content.Context
import android.preference.PreferenceManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class UserCurrency(private val context: Context) {

    private val observable = BehaviorSubject.create<Map<String, String>>()

    init {
        observable.doOnSubscribe {
            observable.onNext(loadCurrencyCodesFromPreferences(context))
        }
    }

    fun getUserCurrencies(): Observable<Map<String, String>> {
        observable.onNext(loadCurrencyCodesFromPreferences(context))
        return observable
    }

    fun refresh() {
        Observable.just("")
                .debounce(500, TimeUnit.MILLISECONDS)
                .delay(1, TimeUnit.SECONDS)
                .subscribe({
                    observable.onNext(loadCurrencyCodesFromPreferences(context))
                })
    }

    private fun loadCurrencyCodesFromPreferences(context: Context): MutableMap<String, String> {
        val codes = context.resources.getStringArray(R.array.available_total_currencies_code)
        val symbols = context.resources.getStringArray(R.array.available_total_currencies_symbol)

        val mainCurrencyCode = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("main_currency_code", "EUR")

        val additionalCurrencyCodes = PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet("selected_currency_codes", HashSet())

        val map: MutableMap<String, String> = HashMap()
        map.put(mainCurrencyCode, symbols[codes.indexOf(mainCurrencyCode)])
        additionalCurrencyCodes.forEach { code -> map.put(code, symbols[codes.indexOf(code)]) }

        return map
    }
}