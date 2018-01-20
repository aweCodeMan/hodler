package si.betoo.hodler.ui.base

import android.app.Application
import com.codemonkeylabs.fpslibrary.TinyDancer
import net.danlew.android.joda.JodaTimeAndroid
import si.betoo.hodler.BuildConfig
import si.betoo.hodler.di.modules.ApplicationModule
import si.betoo.hodler.di.components.ApplicationComponent
import si.betoo.hodler.di.components.DaggerApplicationComponent
import timber.log.Timber

class BaseApplication : Application() {

    companion object {
        @JvmStatic lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            TinyDancer.create()
                    .redFlagPercentage(.1f) // set red indicator for 10%....different from default
                    .startingXPosition(200)
                    .startingYPosition(600)
                    .show(this)
        }

        JodaTimeAndroid.init(this)

        graph = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        graph.inject(this)
    }
}