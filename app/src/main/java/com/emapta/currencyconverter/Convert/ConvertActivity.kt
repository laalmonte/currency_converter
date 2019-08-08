package com.emapta.currencyconverter.Convert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.emapta.currencyconverter.Api.ConvertResponse
import com.emapta.currencyconverter.Base.BaseActivity
import com.emapta.currencyconverter.Base.Constants
import com.emapta.currencyconverter.Cases.CurrencyUseCase
import com.emapta.currencyconverter.CurrencyConverter
import com.emapta.currencyconverter.R

class ConvertActivity : BaseActivity(), ConvertView {
    lateinit var convertPresenter: ConvertPresenter
    var fromAmt = ""
    var toAmt = ""

    private var spinnerFrom: Spinner? = null
    private var spinnerTo: Spinner? = null
    private var btn_convert: Button? = null
    private var et_from_curr_amount: EditText? = null
    private var et_to_curr_amount: EditText? = null

    override val contentView: Int
        get() = R.layout.activity_convert

    override val context: Context
        get() = this

    override val viewContext: Context
        get() = context

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        convertPresenter = ConvertPresenter(this)
        getBinds()
    }

    internal fun getBinds() {
        et_from_curr_amount = findViewById(R.id.et_from_curr_amount)
        et_to_curr_amount = findViewById(R.id.et_to_curr_amount)

        btn_convert = findViewById(R.id.btn_convert)
        btn_convert = findViewById(R.id.btn_convert)

        spinnerFrom = findViewById(R.id.spn_from_curr)
        spinnerTo = findViewById(R.id.spn_to_curr)

        val spinnerArrayAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            Constants.CURRENCY_ARRAY
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom!!.adapter = spinnerArrayAdapter
        spinnerTo!!.adapter = spinnerArrayAdapter

        btn_convert!!.setOnClickListener {
            et_to_curr_amount!!.requestFocus()
            convertPresenter.convertCurrency(
                et_from_curr_amount!!.text.toString().trim { it <= ' ' },
                spinnerFrom!!.selectedItem.toString(),
                spinnerTo!!.selectedItem.toString()
            )
        }
    }

    override fun closeActivity() {

    }

    override fun displayToCurr(convertResponse: ConvertResponse) {
        et_to_curr_amount!!.setText(convertResponse.amount)
        et_from_curr_amount!!.setText("")
    }

    override fun displayErrorToCurr(err: String) {
        et_to_curr_amount!!.setText(err)
        et_from_curr_amount!!.setText("")
    }

    override fun showProgress(show: Boolean) {
        if (show)
            displayProgressDialog(true)
        else
            displayProgressDialog(false)
    }
}
