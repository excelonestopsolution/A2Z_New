package com.a2z.app.ui.screen.util.agreement

import android.content.Context
import android.webkit.JavascriptInterface

class AgreementCallback (private val callback : (Boolean)->Unit){
    @JavascriptInterface
    fun onAgree(isCheck: String) {
        callback(isCheck.toBoolean())
    }
}