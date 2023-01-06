package com.a2z.app.ui.screen.dmt.sender.register

import android.os.Parcelable
import com.a2z.app.data.model.dmt.MoneySender
import com.a2z.app.ui.screen.dmt.util.DMTType
import kotlinx.parcelize.Parcelize

@Parcelize
data class SenderRegistrationArgs(
    val moneySender: MoneySender?,
    val state: String?,
    val registrationType: SenderRegistrationType,
    val dmtType: DMTType
) : Parcelable

