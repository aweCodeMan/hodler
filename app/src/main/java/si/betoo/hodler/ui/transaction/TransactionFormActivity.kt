package si.betoo.hodler.ui.transaction

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
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

    //  Views
    private val toolbar: Toolbar by bindView(R.id.toolbar)

    private val editAmount: EditText by bindView(R.id.edit_amount)
    private val editPricePerUnit: EditText by bindView(R.id.edit_price_per_unit)
    private val editPriceTotal: EditText by bindView(R.id.edit_price_total)

    private val switchTransactionType: Switch by bindView(R.id.switch_transaction_type)

    private val spinnerExchange: Spinner by bindView(R.id.spinner_exchange)
    private val spinnerPair: Spinner by bindView(R.id.spinner_pair)

    private val buttonDate: Button by bindView(R.id.button_date)
    private val buttonSubmit: Button by bindView(R.id.button_submit)

    private val inputFilter: UnsignedDecimalInputFilter = UnsignedDecimalInputFilter()

    private var transactionId: Long = -1
    private lateinit var symbol: String

    companion object {
        private val INTENT_COIN: String = "currencyCode"
        private val INTENT_TRANSACTION_ID: String = "holding_id"

        fun start(context: Activity, coin: Coin, transaction: Transaction?) {
            val starter = Intent(context, TransactionFormActivity::class.java)
            starter.putExtra(INTENT_COIN, coin.symbol)
            starter.putExtra(INTENT_TRANSACTION_ID, transaction?.id)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_form)

        ButterKnife.bind(this)
        graph.inject(this)

        symbol = intent.getStringExtra(TransactionFormActivity.INTENT_COIN)
        transactionId = intent.getLongExtra(TransactionFormActivity.INTENT_TRANSACTION_ID, -1)

        presenter.setupTransactionData(symbol, transactionId)

        setupToolbar(toolbar)

        buttonSubmit.setOnClickListener({
            presenter.saveTransaction()
        })

        switchTransactionType.setOnCheckedChangeListener({ _: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                presenter.onTransactionTypeChanged(Transaction.TYPE_BUY)
            } else {
                presenter.onTransactionTypeChanged(Transaction.TYPE_SELL)
            }
        })

        //  Add filters to input
        editAmount.filters = arrayOf(inputFilter)
        editPriceTotal.filters = arrayOf(inputFilter)
        editPricePerUnit.filters = arrayOf(inputFilter)


        editAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.onAmountChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        editPricePerUnit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.onPricePerUnitChange(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        editPriceTotal.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.onPriceTotalChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.form_transaction_toolbar, menu)

        if (!presenter.canTransactionBeDeleted()) {
            menu.findItem(R.id.action_delete).isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                presenter.onDeleteTransactionClicked()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun showTransactionData(transaction: Transaction) {
        editAmount.setText(transaction.amount.toString())
        editPricePerUnit.setText(transaction.pricePerUnit.toString())
        editPriceTotal.setText(transaction.totalPrice.toString())

        setupDateButton(DateTime(transaction.createdAtMillis))

        switchTransactionType.isChecked = transaction.type == Transaction.TYPE_BUY
    }

    override fun closeView() {
        finish()
    }

    override fun showDeleteTransactionConfirmation(transaction: Transaction) {
        val alert = AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_remove_transaction_title))
                .setMessage(getString(R.string.dialog_remove_transaction_message))

        alert.setNegativeButton(R.string.dialog_remove_transaction_cancel, { _: DialogInterface, _: Int ->

        })

        alert.setPositiveButton(R.string.dialog_remove_transaction_remove, { _: DialogInterface, _: Int ->
            presenter.removeTransaction(transaction)
        })

        alert.show()
    }

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        supportActionBar?.title = getString(R.string.add_transaction)
    }

    override fun loadExchanges(exchanges: Map<String, List<String>>, transaction: Transaction) {
        val exchangeAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, exchanges.keys.toTypedArray())
        exchangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        var pairAdapter: ArrayAdapter<String>? = null

        spinnerExchange.adapter = exchangeAdapter

        spinnerExchange.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerPair.isEnabled = true

                val pairs = exchanges[exchanges.keys.elementAt(position)]
                pairAdapter = ArrayAdapter(view!!.context, android.R.layout.simple_spinner_item, pairs)

                spinnerPair.isEnabled = true
                spinnerPair.adapter = pairAdapter

                presenter.onExchangeSelected(exchanges.keys.elementAt(position))

                transaction.exchangePairSymbol.let {
                    val index = pairAdapter!!.getPosition(transaction.exchangePairSymbol)
                    spinnerPair.setSelection(index)
                }
            }
        }

        spinnerPair.isEnabled = false

        spinnerPair.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (pairAdapter != null) {
                    presenter.onExchangePairSelected(pairAdapter!!.getItem(position))
                }
            }
        }

        transaction.exchange.let {
            val position = exchangeAdapter.getPosition(transaction.exchange)
            spinnerExchange.setSelection(position)
        }
    }

    private fun setupDateButton(selectedDate: DateTime) {
        buttonDate.text = selectedDate.toString(DateTimeFormat.longDateTime())

        buttonDate.setOnClickListener {
            openDatePicker(selectedDate)
        }
    }

    private fun openDatePicker(selectedDateTime: DateTime) {

        val datePicker = DatePickerDialog(this, { datePicker: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val tempDateTime = selectedDateTime.withDate(year, month + 1, dayOfMonth)
            openTimePicker(tempDateTime!!, selectedDateTime)
        }, selectedDateTime.year, selectedDateTime.monthOfYear, selectedDateTime.dayOfMonth)

        datePicker.datePicker.maxDate = System.currentTimeMillis()

        datePicker.show()
    }

    private fun openTimePicker(tempDateTime: DateTime, selectedDateTime: DateTime) {

        val timePicker = TimePickerDialog(this, { timePicker: TimePicker, hour: Int, minute: Int ->
            val temp = tempDateTime.withTime(hour, minute, 0, 0)
            setupDateButton(temp)
            presenter.onDateTimeChanged(temp.millis)
        }, selectedDateTime.hourOfDay, selectedDateTime.minuteOfHour, DateFormat.is24HourFormat(this))

        timePicker.show()
    }

    override fun showProgress(show: Boolean) {

    }

    override fun enableSubmit(enable: Boolean) {
        buttonSubmit.isEnabled = enable
    }
}
