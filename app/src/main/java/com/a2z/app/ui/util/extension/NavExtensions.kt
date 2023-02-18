package com.a2z.app.ui.util.extension

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import com.google.gson.Gson
import java.io.Serializable


inline fun <reified T : Serializable> NavBackStackEntry.singleResult(key: String): T? {

    val arg1 = savedStateHandle.get<T>(key)
    savedStateHandle.remove<T>(key)
    return arg1
}

inline fun <reified T : Parcelable> NavBackStackEntry.singleParcelableResult(key: String): T? {

    val arg1 = savedStateHandle.get<T>(key)
    savedStateHandle.remove<T>(key)
    return arg1
}

inline fun <reified T : Serializable> NavBackStackEntry.safeSerializable(key: String): T? {

    val arg1 = arguments?.getString(key)
    return Gson().fromJson(arg1, T::class.java)
}

inline fun <reified T : Parcelable> NavBackStackEntry.safeParcelable(key: String): T? {
    val rawString = this.arguments?.getString(key)!!
    return Gson().fromJson(rawString, T::class.java)
}

inline fun <reified T : Serializable> SavedStateHandle.safeSerializable(key: String): T? {
    val rawString = this.get<String>(key)!!
    return Gson().fromJson(rawString, T::class.java)
}


inline fun <reified T : Parcelable> SavedStateHandle.safeParcelable(key: String): T? {
    val rawString = this.get<String>(key)!!
    return Gson().fromJson(rawString, T::class.java)
}

