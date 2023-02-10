package com.di_md.a2z.fragment.agreement

import android.webkit.JavascriptInterface

class AgreementCallback (private val callback : (Boolean)->Unit){
    @JavascriptInterface
    fun onAgree(isCheck: String) {
        callback(isCheck.toBoolean())
    }
}