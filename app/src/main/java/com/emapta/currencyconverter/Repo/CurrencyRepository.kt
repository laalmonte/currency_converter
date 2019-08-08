package com.emapta.currencyconverter.Repo

import android.content.Context
import android.util.Log
import com.emapta.currencyconverter.Base.Callback
import com.emapta.currencyconverter.Base.ErrorBundle
import com.emapta.currencyconverter.Cases.CurrencyUseCase
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import io.realm.RealmResults
import io.realm.annotations.Ignore
import io.realm.annotations.RealmClass
import rx.Observable

import java.util.ArrayList
import java.util.concurrent.Callable

@RealmClass
open class CurrencyRepository : RealmModel {
    var id: Long = 0
    var currency: String? = null
    var amount: String? = null

    @Ignore
    private val classRepository = CurrencyRepository::class.java

    @Ignore
    private var mContext: Context? = null

    val currencies: Observable<List<CurrencyUseCase>>
        get() = Observable.fromCallable {
            val cashFloatsUseCaseList = ArrayList<CurrencyUseCase>()
            val realm = Realm.getDefaultInstance()

            val results = realm.where(CurrencyRepository::class.java).findAll()
            if (results != null) {
                for (cf in results) {
                    val currencyUseCase = CurrencyUseCase()
                    currencyUseCase.id = cf.id
                    currencyUseCase.amount = cf.amount
                    currencyUseCase.currency = cf.currency

                    cashFloatsUseCaseList.add(currencyUseCase)
                }
            }

            realm.refresh()
            realm.close()
            cashFloatsUseCaseList
        }

    constructor() {}
    constructor(con: Context) {
        this.mContext = con
    }

    fun executeRecordInsert(currencyUseCase: CurrencyUseCase) {
        Repositories.executeTransaction(object : RepositoryCallback {

            override fun onExecute(realm: Realm) {
                insertUpdateRecord(realm, currencyUseCase)
            }

            override fun onSuccess() {

            }

            override fun onError(errorBundle: ErrorBundle) {

            }

        })
    }

    private fun insertUpdateRecord(realm: Realm, currencyUseCase: CurrencyUseCase): CurrencyRepository? {
        var currencyRepository: CurrencyRepository? =
            realm.where(CurrencyRepository::class.java).equalTo("id", currencyUseCase.id).findFirst()
        if (currencyRepository == null) {
            currencyRepository = realm.createObject(CurrencyRepository::class.java)
        }
        if (currencyRepository != null) {
//            currencyRepository.id = Repositories.getNextId(realm, CurrencyRepository::class.java)
            currencyRepository.id = getNextId(realm)
            currencyRepository.amount = currencyUseCase.amount
            currencyRepository.currency = currencyUseCase.currency

        }
        return currencyRepository
    }

    fun getNextId(realm: Realm): Long {
        val currentIdNum = realm.where(CurrencyRepository::class.java).max("id")
        val result: Int
        if (currentIdNum == null) {
            result = 1
        } else {
            result = currentIdNum!!.toInt() + 1
        }
        return result.toLong()
    }

    fun executeUpdateCurrency(currency: String, newAmount: String) {
        Repositories.executeTransaction(object : RepositoryCallback {

            override fun onExecute(realm: Realm) {
                updateCurrency(realm, currency, newAmount)
            }

            override fun onSuccess() {

            }

            override fun onError(errorBundle: ErrorBundle) {}
        })
    }

    private fun updateCurrency(realm: Realm, currency: String, newAmount: String) {
        val query = realm.where(CurrencyRepository::class.java)
        query.equalTo("currency", currency)
        val results = query.findAll()
        if (results != null) {
            for (cr in results) {
                cr.amount = newAmount
            }
        }
    }
}
