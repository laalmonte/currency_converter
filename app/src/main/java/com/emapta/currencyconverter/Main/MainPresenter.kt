package com.emapta.currencyconverter.Main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import com.emapta.currencyconverter.Base.BasePresenter
import com.emapta.currencyconverter.Base.Constants
import com.emapta.currencyconverter.Cases.CurrencyUseCase
import com.emapta.currencyconverter.CurrencyConverter
import com.emapta.currencyconverter.Repo.CurrencyRepository
import com.emapta.currencyconverter.Repo.Repositories
import rx.Subscriber
import rx.schedulers.Schedulers

import java.util.ArrayList

class MainPresenter internal constructor(mainView: MainView) : BasePresenter<MainView>() {
    private val repositories: Repositories? = null
    private var currencyRepository: CurrencyRepository? = null

    init {
        attachView(mainView)
    }

    internal fun getCurrencies() {
        view!!.showProgress(true)
        currencyRepository = CurrencyRepository(view!!.viewContext)
        currencyRepository!!.currencies
            .compose(this.applySchedulers())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : Subscriber<List<CurrencyUseCase>>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {}

                override fun onNext(currencyUseCases: List<CurrencyUseCase>) {
                    if (currencyUseCases.isEmpty()) {
                        insertInitialCurrency()
                    } else {
                        CurrencyConverter.currencyUseCaseListGlobal = currencyUseCases
                        view!!.loadSavedCurrencies(currencyUseCases)
                        view!!.showProgress(false)
                    }
                }
            })
    }

    private fun insertInitialCurrency() {
        currencyRepository = CurrencyRepository(view!!.viewContext)
        val currencyUseCaseList = ArrayList<CurrencyUseCase>()
        val currencyUseCase1 = CurrencyUseCase()
        currencyUseCase1.id = 1
        currencyUseCase1.currency = "EUR"
        currencyUseCase1.amount = "1000.00"
        currencyUseCaseList.add(currencyUseCase1)

        val currencyUseCase2 = CurrencyUseCase()
        currencyUseCase2.id = 2
        currencyUseCase2.currency = "USD"
        currencyUseCase2.amount = "0.00"
        currencyUseCaseList.add(currencyUseCase2)

        val currencyUseCase3 = CurrencyUseCase()
        currencyUseCase3.id = 3
        currencyUseCase3.currency = "JPY"
        currencyUseCase3.amount = "0.00"
        currencyUseCaseList.add(currencyUseCase3)

        for (cs in currencyUseCaseList) {
            currencyRepository!!.executeRecordInsert(cs)
        }

        val pref = view!!.viewContext.applicationContext.getSharedPreferences("CurrPref", 0)
        val editor = pref.edit()
        editor.putString("count", "0")
        editor.apply()

        CurrencyConverter.currencyUseCaseListGlobal = currencyUseCaseList

        Constants.CURRENCY_ARRAY.add("EUR")
        Constants.CURRENCY_ARRAY.add("USD")
        Constants.CURRENCY_ARRAY.add("JPY")

        Handler().postDelayed({
            view!!.loadSavedCurrencies(currencyUseCaseList)
            view!!.showProgress(false)
        }, 2000)
    }

    internal fun insertNewCurrency(newCurrency: String) {
        view!!.showProgress(true)
        var continueInsert = false
        for (cc in CurrencyConverter.currencyUseCaseListGlobal) {
            if (cc.currency == newCurrency) {
                continueInsert = true
            }
        }

        if (!continueInsert) {
            currencyRepository = CurrencyRepository(view!!.viewContext)
            val currencyUseCaseNew = CurrencyUseCase()
            currencyUseCaseNew.currency = newCurrency
            currencyUseCaseNew.amount = "0.00"
            currencyRepository!!.executeRecordInsert(currencyUseCaseNew)

            val newCount = Constants.CURRENCY_ARRAY.size + 1

            Constants.CURRENCY_ARRAY.add(newCurrency)

            Handler().postDelayed({
                view!!.reloadCurrency()
                view!!.showProgress(false)
            }, 2000)
        } else {
            view!!.showProgress(false)
            view!!.closeAddCurrency()
        }


    }
}
