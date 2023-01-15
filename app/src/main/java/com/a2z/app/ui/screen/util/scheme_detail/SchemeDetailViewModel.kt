package com.a2z.app.ui.screen.util.scheme_detail

import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.model.report.CommissionSchemeDetailResponse
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.safeParcelable

class SchemeDetailViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val data: CommissionSchemeDetailResponse = savedStateHandle.safeParcelable("data")!!
}