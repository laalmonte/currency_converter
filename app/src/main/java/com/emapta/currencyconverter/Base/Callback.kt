package com.emapta.currencyconverter.Base

interface Callback {

    fun onSuccess()

    fun onError(errorBundle: ErrorBundle)

}