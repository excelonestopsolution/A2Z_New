package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class INStaticData(
    val gender : List<INGender>,
    val IdType : List<INIdType>,
    val nationality : INNationality,
    val incomeSource : List<INIncomeSource>,
    val relationShip : List<INRelationShip>,
    val paymentMode : List<INPaymentMode>,
    val purposeOfRemitence : List<INPurposeOfRemitence>,
    val stateLists : List<INState>,
    val bankName : List<INBankName>,
    val customerType : List<INCustomerType>,
    val sourceIncomeType : List<INSourceIncomeType>,
    val annualIncome : List<INAnnualIncome>,
    val senderProofType : List<INSenderProofType>,

) : Parcelable

@Keep
@Parcelize
data class INGender(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INIdType(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INNationality(
    val id : String,
    val name : String
) : Parcelable
@Keep
@Parcelize
data class INIncomeSource(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INRelationShip(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INPaymentMode(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INPurposeOfRemitence(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INState(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INBankName(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INCustomerType(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INSourceIncomeType(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INAnnualIncome(
    val id : String,
    val name : String
) : Parcelable

@Keep
@Parcelize
data class INSenderProofType(
    val id : String,
    val name : String
) : Parcelable