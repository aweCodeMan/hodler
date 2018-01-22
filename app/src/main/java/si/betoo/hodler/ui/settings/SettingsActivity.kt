package si.betoo.hodler.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.*
import android.view.MenuItem
import si.betoo.hodler.R
import si.betoo.hodler.UserSettings
import si.betoo.hodler.ui.base.BaseActivity
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    @Inject
    lateinit var userSettings: UserSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()

        graph.inject(this)

        fragmentManager.beginTransaction().replace(android.R.id.content, GeneralPreferenceFragment()).commit()
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class GeneralPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.settings)
            setHasOptionsMenu(true)

            bindPreferenceSummaryToValue(findPreference("main_currency_code"))
            bindPreferenceSummaryToValue(findPreference("selected_currency_codes"))
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }

        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()

            if (preference is ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                val listPreference = preference
                val index = listPreference.findIndexOfValue(stringValue)

                // Set the summary to reflect the new value.
                preference.setSummary(
                        if (index >= 0)
                            preference.context.resources.getString(R.string.selected_preference, listPreference.entries[index])
                        else
                            preference.context.resources.getString(R.string.nothing_selected_preference)
                )

            } else if (preference is MultiSelectListPreference) {
                val set = value as MutableSet<*>

                val list = set.map { preference.entries[preference.findIndexOfValue(it.toString())] }

                preference.setSummary(
                        if (list.isNotEmpty())
                            preference.context.resources.getString(R.string.selected_preference, list.joinToString(", "))
                        else
                            preference.context.resources.getString(R.string.none_selected_preference))
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.summary = stringValue
            }

            (activity as SettingsActivity).userSettings.refresh()

            true
        }


        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.

         * @see .sBindPreferenceSummaryToValueListener
         */
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the listener to watch for value changes.
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            // Trigger the listener immediately with the preference's
            // current value.
            if (preference is MultiSelectListPreference) {
                sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                        PreferenceManager
                                .getDefaultSharedPreferences(preference.context)
                                .getStringSet(preference.key, HashSet()))
            } else {
                sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                        PreferenceManager
                                .getDefaultSharedPreferences(preference.context)
                                .getString(preference.key, ""))
            }

        }
    }

    companion object {
        fun start(context: Activity) {
            val starter = Intent(context, SettingsActivity::class.java)
            context.startActivity(starter)
        }
    }
}
