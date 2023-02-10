package com.a2z.app.fragment.agreement

import android.webkit.JavascriptInterface

class AgreementCallback (private val callback : (Boolean)->Unit){
    @JavascriptInterface
    fun onAgree(isCheck: String) {
        callback(isCheck.toBoolean())
    }
}