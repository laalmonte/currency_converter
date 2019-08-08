package com.emapta.currencyconverter.Base

import com.emapta.currencyconverter.Api.ApiService
import io.realm.Realm
import retrofit2.Call
import rx.Observable
import rx.Scheduler
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

open class BasePresenter<V> {

    protected var realm: Realm? = null
    protected var apiService: ApiService? = null
    protected var key: String? = null
    protected var call: Call<*>? = null
    protected var subscription: Subscription? = null

    var view: V? = null
        private set
    val isViewAttached: Boolean
        get() = view != null

    protected var mExecutionThread = Schedulers.io()
    protected var mPostExecutionThread = AndroidSchedulers.mainThread()
    fun attachView(view: V) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    protected fun <T> applySchedulers(): Observable.Transformer<T, T> {
        return Observable.Transformer { tObservable ->
            tObservable.subscribeOn(mExecutionThread)
                .observeOn(mPostExecutionThread)
        }
    }

    fun cancelProcess() {
        if (call != null && !call!!.isCanceled) {
            call!!.cancel()
        }

        if (subscription != null && !subscription!!.isUnsubscribed) {
            subscription!!.unsubscribe()
        }
    }
}
