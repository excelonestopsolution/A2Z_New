package com.a2z.app.ui.screen.dashboard

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.local.AppPreference
import com.a2z.app.ui.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    saveStateHandle: SavedStateHandle,
    val appPreference: AppPreference
) : BaseViewModel() {

    var scaffoldState : ScaffoldState? = null
    var fromLogin = saveStateHandle.get<String?>("fromLogin").toBoolean()

    val bottomSheetVisibilityState = mutableStateOf(false)

}