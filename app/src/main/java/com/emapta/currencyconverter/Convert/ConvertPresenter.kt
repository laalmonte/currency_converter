package com.emapta.currencyconverter.Convert

import android.content.SharedPreferences
import android.util.Log
import com.emapta.currencyconverter.Api.ConvertResponse
import com.emapta.currencyconverter.Base.*
import com.emapta.currencyconverter.Cases.CurrencyUseCase
import com.emapta.currencyconverter.CurrencyConverter
import com.emapta.currencyconverter.Repo.CurrencyRepository
import com.emapta.currencyconverter.Repo.Repositories
import rx.Subscriber
import rx.schedulers.Schedulers

import java.text.DecimalFormat

class ConvertPresenter internal constructor(convertView: ConvertView) : BasePresenter<ConvertView>() {
    private var repositories: Repositories? = null
    private var currencyRepository: CurrencyRepository? = null

    private val convertCount: String?
        get() {
            val pref = view!!.viewContext.applicationContext.getSharedPreferences("CurrPref", 0)
            return pref.getString("count", "0")
        }

    init {
        attachView(convertView)
    }

    internal fun convertCurrency(amt: String, fromCurr: String, toCurr: String) {
        view!!.showProgress(true)
        repositories = Repositories(CurrencyConverter.BASE_URL)
        repositories!!.processConvertion(fromCurr, toCurr, amt)
            .compose(this.applySchedulers())
            .subscribe(DefaultSubscriber(object : ObjectCallback<ConvertResponse> {
                override fun onSuccess(convertResponse: ConvertResponse) {
                    if (convertResponse.currency == fromCurr) {
                        if (isViewAttached) view!!.displayErrorToCurr(Constants.SAME_CURRENCY)
                        view!!.showProgress(false)
                    } else {
                        compute(amt, fromCurr, convertResponse)
                    }

                }

                override fun onError(errorBundle: ErrorBundle) {
                    Log.e("ERROR", errorBundle.toString())
                    if (isViewAttached) view!!.displayErrorToCurr(errorBundle.errorMessage!!)
                    view!!.showProgress(false)
                }
            }))
    }

    private fun compute(amountUsed: String, fromCurr: String, convertResponse: ConvertResponse) {

        var count = Integer.parseInt(convertCount!!)

        if (count > 5) {
            updateCompute(true, amountUsed, fromCurr, convertResponse)
        } else {
            updateCompute(false, amountUsed, fromCurr, convertResponse)
            count++
            saveSharedPrefs(count)
        }
    }

    private fun saveSharedPrefs(cnt: Int) {
        val pref = view!!.viewContext.applicationContext.getSharedPreferences("CurrPref", 0)
        val editor = pref.edit()
        editor.putString("count", cnt.toString())
        editor.apply()
    }

    private fun updateCompute(
        greaterThanFive: Boolean,
        amountUsed: String,
        fromCurr: String,
        convertResponse: ConvertResponse
    ) {
        var doubleAmtFrom = 0.00
        var doubleAmtTo = 0.00
        var convenienceFee = 0.00
        var oldAmountFrom = 0.00
        var oldAmountTo = 0.00
        var newAmountFrom = 0.00
        var newAmountTo = 0.00

        try {
            doubleAmtFrom = java.lang.Double.parseDouble(amountUsed)

            if (greaterThanFive) {
                convenienceFee = doubleAmtFrom * 0.07
                doubleAmtFrom = doubleAmtFrom + convenienceFee
            }

            doubleAmtTo = java.lang.Double.parseDouble(convertResponse.amount!!)
            var oldAmountFromString: String? = ""
            var oldAmountToString: String? = ""
            for (cs in CurrencyConverter.currencyUseCaseListGlobal) {
                if (cs.currency == fromCurr)
                    oldAmountFromString = cs.amount
                if (cs.currency == convertResponse.currency)
                    oldAmountToString = cs.amount
            }

            oldAmountFrom = java.lang.Double.parseDouble(oldAmountFromString!!)
            oldAmountTo = java.lang.Double.parseDouble(oldAmountToString!!)

            newAmountFrom = oldAmountFrom - doubleAmtFrom

            currencyRepository = CurrencyRepository(view!!.viewContext)

            var formattedFrom = ""
            var formattedTo = ""

            if (newAmountFrom < 0) {
                view!!.displayErrorToCurr(Constants.INSUFFICIENT_FUNDS)
            } else {
                formattedFrom = CurrencyConverter.convertToDecimalFormat(newAmountFrom)
                currencyRepository!!.executeUpdateCurrency(fromCurr, formattedFrom)
                newAmountTo = oldAmountTo + doubleAmtTo
                formattedTo = CurrencyConverter.convertToDecimalFormat(newAmountTo)
                currencyRepository!!.executeUpdateCurrency(convertResponse.currency!!, formattedTo)
                for (cs in CurrencyConverter.currencyUseCaseListGlobal) {
                    if (cs.currency == fromCurr)
                        cs.amount = formattedFrom
                    if (cs.currency == convertResponse.currency)
                        cs.amount = formattedTo
                }

                if (isViewAttached) view!!.displayToCurr(convertResponse)
            }

        } catch (e: Exception) {
            Log.e("convert", "double amount error")
        }

        view!!.showProgress(false)
    }

}
