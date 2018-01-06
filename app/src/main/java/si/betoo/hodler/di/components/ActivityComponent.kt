package si.betoo.hodler.di.components

import dagger.Component
import si.betoo.hodler.di.modules.ActivityModule
import si.betoo.hodler.di.scopes.ForActivity
import si.betoo.hodler.ui.select.SelectCoinsActivity
import si.betoo.hodler.ui.main.MainActivity

@ForActivity
@Component(modules = arrayOf(ActivityModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: SelectCoinsActivity)


    /*  fun mainView(): MainMVP.View

      fun addView(): SelectCoinsMVP.View

      fun mainPresenter(view: MainMVP.Presenter): MainMVP.Presenter

      fun addPresenter(view: SelectCoinsMVP.Presenter): SelectCoinsMVP.Presenter*/

}