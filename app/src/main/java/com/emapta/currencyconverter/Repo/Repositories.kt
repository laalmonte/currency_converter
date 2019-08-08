package com.emapta.currencyconverter.Repo

import android.util.Log
import com.emapta.currencyconverter.Api.ApiService
import com.emapta.currencyconverter.Api.ConvertResponse
import com.emapta.currencyconverter.Api.DataWrapper
import com.emapta.currencyconverter.Base.ErrorBundle
import com.emapta.currencyconverter.Base.Callback
import com.emapta.currencyconverter.CurrencyConverter
import io.realm.Realm
import io.realm.RealmResults
import io.realm.annotations.RealmModule
import retrofit2.Response
import rx.Observable
import rx.functions.Func1

@RealmModule(library = true, classes = [CurrencyRepository::class])
class Repositories {
    private var apiService: ApiService? = null

    constructor() {}
    constructor(url: String) {
        this.apiService = ApiService(url)
    }

    fun processConvertion(fromCurr: String, toCurr: String, amtToConvert: String): Observable<ConvertResponse> {
        return apiService!!.convertCurrency().convert(amtToConvert, fromCurr, toCurr)
            .flatMap { convertResponseResponse -> Observable.just(convertResponseResponse.body()) }

    }

    companion object {


        fun executeTransaction(callback: RepositoryCallback) {
            val realm = Realm.getDefaultInstance()
            try {
                realm!!.executeTransaction { realm -> callback.onExecute(realm) }

            } catch (e: Exception) {

                val errorBundle = ErrorBundle(e.toString())
                callback.onError(errorBundle)

            } finally {
                realm?.close()

                callback.onSuccess()
            }
        }
    }
}


