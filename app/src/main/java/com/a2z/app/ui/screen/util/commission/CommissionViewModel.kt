package com.a2z.app.ui.screen.util.commission

import androidx.lifecycle.viewModelScope
import com.a2z.app.data.model.report.CommissionScheme
import com.a2z.app.data.model.report.CommissionSchemeDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeListResponse
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommissionViewModel @Inject constructor(
    private val repository: AppRepository
) : BaseViewModel() {


    val schemeResponseResultFlow = resultStateFlow<CommissionSchemeListResponse>()
    private val _schemeDetailResultFlow = resultShareFlow<CommissionSchemeDetailResponse>()

    init {
        fetchSchemeList()

        viewModelScope.launch {
            _schemeDetailResultFlow.getLatest {
                if (it.status == 1) {
                    navigateTo(
                        NavScreen.SchemeDetailScreen.passArgs(
                            it
                        )
                    )
                } else failureDialog(it.message)
            }
        }
    }

    private fun fetchSchemeList() {
        callApiForStateFlow(
            flow = schemeResponseResultFlow,
            call = { repository.schemeList() }
        )
    }

    fun onSchemeItemClick(it: CommissionScheme) {

        callApiForShareFlow(
            flow = _schemeDetailResultFlow,
            call = {
                repository.schemeDetail(
                    hashMapOf(
                        "type" to it.type
                    )
                )
            }
        )
    }

}