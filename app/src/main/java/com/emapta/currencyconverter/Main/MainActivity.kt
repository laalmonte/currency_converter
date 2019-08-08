package com.emapta.currencyconverter.Main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.emapta.currencyconverter.Base.BaseActivity
import com.emapta.currencyconverter.Base.Constants
import com.emapta.currencyconverter.Cases.CurrencyUseCase
import com.emapta.currencyconverter.Convert.ConvertActivity
import com.emapta.currencyconverter.R

class MainActivity : BaseActivity(), MainView {
    private var ll_wallet: LinearLayout? = null
    private var lv_currencies: ListView? = null
    private val mainPresenter = MainPresenter(this)
    private var showRv = false

    override val contentView: Int
        get() = R.layout.activity_main

    override val context: Context
        get() = this

    override val viewContext: Context
        get() = context

    override fun onResume() {
        super.onResume()
        mainPresenter?.getCurrencies()
        lv_currencies!!.visibility = View.GONE
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        getBinds()
    }

    internal fun getBinds() {
        ll_wallet = findViewById(R.id.ll_wallet)
        lv_currencies = findViewById(R.id.lv_currencies)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, Constants.TO_ADD
        )
        lv_currencies!!.adapter = adapter

        lv_currencies!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val s = lv_currencies!!.getItemAtPosition(position).toString()
            mainPresenter.insertNewCurrency(s)
            lv_currencies!!.visibility = View.GONE
        }

        findViewById<View>(R.id.btn_convert_curr).setOnClickListener {
            val convActivityIntent = Intent(context, ConvertActivity::class.java)
            convActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(convActivityIntent)
        }

        findViewById<View>(R.id.btn_add_curr).setOnClickListener {
            if (showRv) {
                lv_currencies!!.visibility = View.GONE
                showRv = false
            } else {
                lv_currencies!!.visibility = View.VISIBLE
                showRv = true
            }
        }
    }


    override fun loadSavedCurrencies(currencyUseCaseList: List<CurrencyUseCase>) {
        ll_wallet!!.removeAllViews()
        if (currencyUseCaseList != null) {
            if (!currencyUseCaseList.isEmpty()) {
                for (cs in currencyUseCaseList) {
                    val ll_new = LinearLayout(this)
                    ll_new.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    ll_new.weightSum = 2f

                    val params = LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    params.weight = 1f

                    val tt1 = TextView(this)
                    tt1.layoutParams = params
                    tt1.text = cs.currency
                    tt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    tt1.setTextColor(ContextCompat.getColor(this, R.color.black))

                    val tt2 = TextView(this)
                    tt2.layoutParams = params
                    tt2.text = cs.amount
                    tt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    tt2.setTextColor(ContextCompat.getColor(this, R.color.black))


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        tt2.gravity = View.TEXT_ALIGNMENT_VIEW_END
                        tt1.gravity = View.TEXT_ALIGNMENT_CENTER
                        ll_new.gravity = View.TEXT_ALIGNMENT_CENTER
                    }

                    ll_new.addView(tt1)
                    ll_new.addView(tt2)

                    ll_wallet!!.addView(ll_new)
                }
            }
        }
    }

    override fun showProgress(show: Boolean) {
        if (show)
            displayProgressDialog(true)
        else
            displayProgressDialog(false)
    }

    override fun reloadCurrency() {
        onResume()
    }

    override fun closeAddCurrency() {
        lv_currencies!!.visibility = View.GONE
    }

    override fun closeActivity() {
        finish()
    }
}
