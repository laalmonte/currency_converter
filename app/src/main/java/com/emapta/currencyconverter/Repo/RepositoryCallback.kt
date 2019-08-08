package com.emapta.currencyconverter.Repo

import com.emapta.currencyconverter.Base.ErrorBundle
import io.realm.Realm

interface RepositoryCallback {
    fun onExecute(realm: Realm)
    fun onSuccess()
    fun onError(errorBundle: ErrorBundle)
}

