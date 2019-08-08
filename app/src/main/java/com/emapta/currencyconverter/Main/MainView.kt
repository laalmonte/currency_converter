package com.emapta.currencyconverter.Main

import android.content.Context
import com.emapta.currencyconverter.Base.BaseView
import com.emapta.currencyconverter.Cases.CurrencyUseCase

interface MainView : BaseView {
    val viewContext: Context
    fun loadSavedCurrencies(currencyUseCaseList: List<CurrencyUseCase>)
    fun showProgress(show: Boolean)
    fun reloadCurrency()
    fun closeAddCurrency()
}
