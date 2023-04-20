package com.a2z.app.data.model.auth

import android.os.Parcelable
import androidx.annotation.Keep
import com.a2z.app.ui.util.resource.StatusDialogType
import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
data class AepsDriver (
    val driver_name : String,
    val package_name : String
    ) : Parcelable