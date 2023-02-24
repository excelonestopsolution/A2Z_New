package com.a2z.app.data.model.indonepal

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.common.collect.Lists
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class INBranchResponse(
    val status : Int,
    val message : String?,
    val data : List<INBranch>?

) : Parcelable

@Keep
@Parcelize
data class INBranch(
    val branchId : String,
    val branchName : String,
    val branchCode : String,
) : Parcelable