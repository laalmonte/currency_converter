package com.emapta.currencyconverter.Base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import butterknife.ButterKnife

abstract class BaseDialogFragment : DialogFragment() {
//    protected lateinit var isTransparent = false
    protected var isItTransparent: Boolean = false
    protected lateinit var mActivity: Activity
//    protected lateinit var context: Context
    protected lateinit var mContext: Context

    protected abstract val fragmentDialog: Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog.window != null) {
            if (isItTransparent) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return dialog
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun dismiss() {
        if (getContext() != null)
            super.dismiss()
    }

    override fun onAttach(cont: Context) {
        super.onAttach(cont)
        this.mContext = cont
        if (cont is Activity) {
            mActivity = cont
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(fragmentDialog, container, false)

        if (view != null) {
            ButterKnife.bind(this, view)
            if (dialog.window != null) {
                if (isItTransparent) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            }
            onCreateDialogReady(inflater, container, savedInstanceState)
        }
        return view
    }

    protected abstract fun onCreateDialogReady(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )

    protected fun cancelable(isCancelable: Boolean) {
        setCancelable(isCancelable)
    }

    fun setTransparent(isTransparent: Boolean) {
        this.isItTransparent = isTransparent
    }
}
