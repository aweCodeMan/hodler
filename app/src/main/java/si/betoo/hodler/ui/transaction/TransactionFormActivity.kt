package si.betoo.hodler.ui.transaction

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import butterknife.ButterKnife
import kotterknife.bindView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import si.betoo.hodler.R
import si.betoo.hodler.data.coin.Coin
import si.betoo.hodler.data.coin.Transaction
import si.betoo.hodler.ui.base.BaseActivity
import javax.inject.Inject

class TransactionFormActivity : BaseActivity(), TransactionFormMVP.View {


    @Inject
    lateinit var presenter: TransactionFormMVP.Presenter

    private lateinit var symbol: String

    //  Views
    private val toolbar: Toolbar by bindView(R.id.toolbar)

    private val amount: EditText by bindView(R.id.edit_amount)
    private val date: Button by bindView(R.id.button_date)

    private val submit: Button by bindView(R.id.button_submit)
    private var holdingId: Long = -1

    private var selectedDateTime = DateTime(System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_form)

        ButterKnife.bind(this)
        graph.inject(this)

        symbol = intent.getStringExtra(TransactionFormActivity.INTENT_COIN)
        holdingId = intent.getLongExtra(TransactionFormActivity.INTENT_HOLDING, -1)

        if (holdingId > 0) {
            presenter.loadHolding(holdingId)

        }

        setupToolbar(toolbar)

        submit.setOnClickListener { presenter.saveHolding(symbol, amount.text.toString().toDouble()) }

        setupDateButton(selectedDateTime)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.form_transaction_toolbar, menu)

        if (holdingId <= 0) {
            menu.findItem(R.id.action_delete).isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                presenter.onDeleteClicked()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val INTENT_COIN: String = "symbol"
        private val INTENT_HOLDING: String = "holding_id"

        fun start(context: Activity, coin: Coin, transaction: Transaction?) {
            val starter = Intent(context, TransactionFormActivity::class.java)
            starter.putExtra(INTENT_COIN, coin.symbol)
            starter.putExtra(INTENT_HOLDING, transaction?.id)
            context.startActivity(starter)
        }
    }

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        supportActionBar?.title = getString(R.string.add_transaction)
    }

    override fun loadHolding(transaction: Transaction) {
        amount.setText(transaction.amount.toString())
    }

    override fun closeView() {
        finish()
    }

    override fun showDeleteConfirmation(transaction: Transaction) {
        val alert = AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_remove_transaction_title))
                .setMessage(getString(R.string.dialog_remove_transaction_message))

        alert.setNegativeButton(R.string.dialog_remove_transaction_cancel, DialogInterface.OnClickListener({ dialogInterface: DialogInterface, i: Int ->

        }))

        alert.setPositiveButton(R.string.dialog_remove_transaction_remove, DialogInterface.OnClickListener({ dialogInterface: DialogInterface, i: Int ->
            presenter.removeTransaction(transaction)
        }))

        alert.show()
    }

    private fun setupDateButton(selectedDate: DateTime) {
        date.text = selectedDate.toString(DateTimeFormat.longDateTime())

        date.setOnClickListener {
            openDatePicker()
        }

    }

    private fun openDatePicker() {
        var tempDateTime: DateTime? = null

        val datePicker = DatePickerDialog(this, { datePicker: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            tempDateTime = selectedDateTime.withDate(year, month + 1, dayOfMonth)
            openTimePicker(tempDateTime!!)
        }, selectedDateTime.year, selectedDateTime.monthOfYear, selectedDateTime.dayOfMonth)

        datePicker.datePicker.maxDate = System.currentTimeMillis()

        datePicker.show()
    }

    private fun openTimePicker(tempDateTime: DateTime) {

        val timePicker = TimePickerDialog(this, { timePicker: TimePicker, hour: Int, minute: Int ->
            selectedDateTime = tempDateTime.withTime(hour, minute, 0, 0)
            setupDateButton(selectedDateTime)
        }, selectedDateTime.hourOfDay, selectedDateTime.minuteOfHour, DateFormat.is24HourFormat(this))

        timePicker.show()
    }
}
