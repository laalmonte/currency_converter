package com.emapta.currencyconverter

import android.provider.SyncStateContract
import com.emapta.currencyconverter.Cases.CurrencyUseCase

import java.text.DecimalFormat
import java.util.ArrayList

class CurrencyConverter : SyncStateContract.Constants() {
    companion object {
        val BASE_URL = "http://api.evp.lt/currency/commercial/exchange/"
        var currencyUseCaseListGlobal: List<CurrencyUseCase> = ArrayList()


        fun convertToDecimalFormat(value: Double): String {
            val df = DecimalFormat("#.##")
            return df.format(value)
        }
    }
}
