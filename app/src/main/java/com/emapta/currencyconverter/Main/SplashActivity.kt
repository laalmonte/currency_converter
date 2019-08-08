package com.emapta.currencyconverter.Main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.emapta.currencyconverter.Base.BaseActivity
import com.emapta.currencyconverter.Main.MainActivity
import com.emapta.currencyconverter.R

class SplashActivity : BaseActivity() {
    override val contentView: Int
        get() = R.layout.activity_splash

    override val context: Context
        get() = this

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        changeActivity()
    }

    internal fun changeActivity() {
        Handler().postDelayed({
            val mainActivityIntent = Intent(context, MainActivity::class.java)
            mainActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(mainActivityIntent)

            finish()
        }, 2000)
    }
}