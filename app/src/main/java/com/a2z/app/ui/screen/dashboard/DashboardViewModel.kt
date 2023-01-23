package com.a2z.app.ui.screen.dashboard

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.ui.screen.report.ledger.LedgerReportViewModel
import com.a2z.app.ui.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    saveStateHandle: SavedStateHandle,
    val appPreference: AppPreference
) : BaseViewModel() {

    var scaffoldState: ScaffoldState? = null
    var fromLogin = saveStateHandle.get<String?>("fromLogin").toBoolean()

    val bottomSheetVisibilityState = mutableStateOf(false)


}