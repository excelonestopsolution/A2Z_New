package com.a2z.app.util.extension

import android.content.Context
import android.widget.Toast

fun Context?.showToast(message : String?){
    Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()
}