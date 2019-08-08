package com.emapta.currencyconverter.Base

import android.provider.SyncStateContract
import com.emapta.currencyconverter.Repo.Repositories
import io.realm.Realm
import io.realm.RealmConfiguration

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name(Realm.DEFAULT_REALM_NAME)
            .schemaVersion(0)
            .modules(Repositories())
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

}
