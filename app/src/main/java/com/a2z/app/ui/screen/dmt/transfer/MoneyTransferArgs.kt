package com.a2z.app.ui.screen.dmt.transfer

import android.os.Parcelable
import com.a2z.app.data.model.dmt.Beneficiary
import com.a2z.app.data.model.dmt.MoneySender
import com.a2z.app.ui.screen.dmt.util.DMTType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoneyTransferArgs(
    val moneySender: MoneySender,
    val beneficiary: Beneficiary,
    val dmtType: DMTType
) : Parcelable

