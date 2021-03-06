package si.betoo.hodler.di.components

import dagger.Component
import si.betoo.hodler.di.modules.ActivityModule
import si.betoo.hodler.di.scopes.ForActivity
import si.betoo.hodler.ui.detail.CoinDetailActivity
import si.betoo.hodler.ui.transaction.TransactionFormActivity
import si.betoo.hodler.ui.select.SelectCoinsActivity
import si.betoo.hodler.ui.main.MainActivity
import si.betoo.hodler.ui.settings.SettingsActivity

@ForActivity
@Component(modules = [(ActivityModule::class)], dependencies = [(ApplicationComponent::class)])
interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: SelectCoinsActivity)
    fun inject(activity: CoinDetailActivity)
    fun inject(activity: TransactionFormActivity)
    fun inject(activity: SettingsActivity)
}