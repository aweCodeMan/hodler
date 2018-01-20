package si.betoo.hodler.ui.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import si.betoo.hodler.di.*
import si.betoo.hodler.di.components.ActivityComponent
import si.betoo.hodler.di.components.DaggerActivityComponent
import si.betoo.hodler.di.modules.ActivityModule

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        @JvmStatic lateinit var graph: ActivityComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        graph = DaggerActivityComponent
                .builder()
                .activityModule(ActivityModule(this))
                .applicationComponent(BaseApplication.graph)
                .build()

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}