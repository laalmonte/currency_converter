package com.emapta.currencyconverter.Base

interface ObjectCallback<T> {
    fun onSuccess(type: T)
    fun onError(errorBundle: ErrorBundle)
}
