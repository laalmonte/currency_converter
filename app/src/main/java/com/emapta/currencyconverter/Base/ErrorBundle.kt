package com.emapta.currencyconverter.Base

import java.io.IOException

class ErrorBundle : Exception {

    var errorMessage: String? = null
        get() {
            if (field != null && !field!!.isEmpty()) {
                return field
            }
            errorMessage = super.getLocalizedMessage()
            return if (field != null) field else "Unknown error."
        }
        protected set

    constructor(message: String) : super(message) {}

    constructor(throwable: Throwable) : super(throwable) {
        if (throwable is IOException) {
            errorMessage = "No Internet Connection"
        } else {
            errorMessage = "Invalid Amount"
        }
    }

}
