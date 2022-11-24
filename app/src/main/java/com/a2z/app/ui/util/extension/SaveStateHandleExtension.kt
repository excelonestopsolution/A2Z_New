package com.a2z.app.ui.util.extension

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import java.io.Serializable


inline fun <reified T : Parcelable> SavedStateHandle.getSafeParcelable(): T? {
    val key: String = T::class.java.simpleName
    return  this[key]
}

inline fun <reified T : Serializable> SavedStateHandle.getSafeSerializable(): T? {
    val key: String = T::class.java.simpleName
    return  this[key]
}