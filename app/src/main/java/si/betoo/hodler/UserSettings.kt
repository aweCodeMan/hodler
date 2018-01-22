package si.betoo.hodler

import android.content.Context
import android.preference.PreferenceManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class UserSettings(private val context: Context) {

    private val observableUserCurrencies = BehaviorSubject.create<Map<String, String>>()
    private val observableMainCurrency = BehaviorSubject.create<Map<String, String>>()

    init {
        observableUserCurrencies.doOnSubscribe {
            observableUserCurrencies.onNext(loadCurrencyCodesFromPreferences(context))
        }

        observableMainCurrency.doOnSubscribe {
            observableMainCurrency.onNext(loadMainCurrency(context))
        }
    }

    fun getUserCurrencies(): Observable<Map<String, String>> {
        observableUserCurrencies.onNext(loadCurrencyCodesFromPreferences(context))
        return observableUserCurrencies
    }

    fun getMainCurrency(): Observable<Map<String, String>> {
        observableMainCurrency.onNext(loadMainCurrency(context))
        return observableMainCurrency
    }

    fun refresh() {
        Observable.just("")
                .debounce(500, TimeUnit.MILLISECONDS)
                .delay(1, TimeUnit.SECONDS)
                .subscribe({
                    observableUserCurrencies.onNext(loadCurrencyCodesFromPreferences(context))
                    observableMainCurrency.onNext(loadMainCurrency(context))
                })
    }

    private fun loadMainCurrency(context: Context): Map<String, String> {
        val codes = context.resources.getStringArray(R.array.available_total_currencies_code)
        val symbols = context.resources.getStringArray(R.array.available_total_currencies_symbol)

        val mainCurrencyCode = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("main_currency_code", "EUR")

        val map: MutableMap<String, String> = HashMap()
        map.put(mainCurrencyCode, symbols[codes.indexOf(mainCurrencyCode)])

        return map
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