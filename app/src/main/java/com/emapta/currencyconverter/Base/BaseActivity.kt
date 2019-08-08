package com.emapta.currencyconverter.Base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import butterknife.ButterKnife

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val contentView: Int
    protected abstract val context: Context

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        ButterKnife.bind(context as Activity)
        onViewReady(savedInstanceState, intent)
    }

    protected abstract fun onViewReady(savedInstanceState: Bundle?, intent: Intent)

    protected fun displayProgressDialog(showProgressDialog: Boolean) {
        try {
            if (showProgressDialog) {
                val fragmentManager = supportFragmentManager
                if (fragmentManager != null) {
                    val dialogFragment = ProgressDialog()
                    dialogFragment.isCancelable = false
                    dialogFragment.show(fragmentManager, "DIAG_PROGRESS")
                }
            } else {
                val fragmentManager = supportFragmentManager
                fragmentManager.executePendingTransactions()
                val previous = fragmentManager.findFragmentByTag("DIAG_PROGRESS")
                if (previous != null) {
                    val dialogFragment = previous as DialogFragment?
                    dialogFragment!!.dismiss()
                }
            }
        } catch (e: Exception) {
        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

