package com.a2z.app.ui.screen.util.pan_service

import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.PanCardServiceNoteResponse
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PanServiceViewModel @Inject constructor(
    private val repository: AppRepository,
    private val appPreference: AppPreference
) : BaseViewModel() {


    val serviceNoteStateFlow = resultStateFlow<PanCardServiceNoteResponse>()
    private val _serviceActivateFlow = resultShareFlow<AppResponse>()

    val confirmDialogState = mutableStateOf(false)

    init {
        fetchServiceNote()

        _serviceActivateFlow.getLatest {
            if (it.status == 1) {
                appPreference.user = appPreference.user?.copy(
                    isPanCardActivated = 1
                )
                successDialog(it.message) {
                    navigateTo(NavScreen.DashboardScreen.route, true)
                }
            } else failureDialog(it.message)
        }

    }

    private fun fetchServiceNote() {
        callApiForStateFlow(
            flow = serviceNoteStateFlow,
            call = { repository.panServiceNote() }
        )
    }

    fun onActivate() {
        confirmDialogState.value = true
    }

    fun onConfirmActivation() {

        val param = hashMapOf("na" to "na")
        callApiForShareFlow(_serviceActivateFlow) { repository.panServiceActivate(param) }
    }

}