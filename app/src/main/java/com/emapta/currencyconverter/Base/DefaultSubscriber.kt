package com.emapta.currencyconverter.Base

import rx.Subscriber

class DefaultSubscriber<T> : Subscriber<T> {

    // Nullable
    private var mCallback: Callback? = null

    // Nullable
    private var mObjectCallback: ObjectCallback<T>? = null

    constructor() {}

    constructor(callback: Callback) {
        mCallback = callback
    }

    constructor(objectCallback: ObjectCallback<T>) {
        mObjectCallback = objectCallback
    }

    override fun onCompleted() {
        mCallback?.onSuccess()
    }

    override fun onError(e: Throwable) {
        val errorBundle: ErrorBundle
        if (e is ErrorBundle) {
            errorBundle = e
        } else {
            errorBundle = ErrorBundle(e)
        }

        mCallback?.onError(errorBundle)
        mObjectCallback?.onError(errorBundle)
    }

    override fun onNext(`object`: T) {
        mObjectCallback?.onSuccess(`object`)
    }
}
