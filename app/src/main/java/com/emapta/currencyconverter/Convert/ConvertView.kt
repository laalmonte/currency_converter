package com.emapta.currencyconverter.Convert

import android.content.Context
import com.emapta.currencyconverter.Api.ConvertResponse
import com.emapta.currencyconverter.Base.BaseView
import com.emapta.currencyconverter.Cases.CurrencyUseCase


interface ConvertView : BaseView {
    val viewContext: Context
    fun displayToCurr(convertResponse: ConvertResponse)
    fun displayErrorToCurr(err: String)
    fun showProgress(show: Boolean)
}

