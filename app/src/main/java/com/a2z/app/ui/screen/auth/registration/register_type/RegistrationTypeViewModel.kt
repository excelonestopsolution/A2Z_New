package com.a2z.app.ui.screen.auth.registration.register_type

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.auth.RegistrationRole
import com.a2z.app.data.model.auth.RegistrationRoleResponse
import com.a2z.app.data.model.auth.RegistrationRoleUser
import com.a2z.app.data.repository.RegistrationRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationTypeViewModel @Inject constructor(
    private val repository: RegistrationRepository,
    val appPreference: AppPreference,
    saveState: SavedStateHandle
) : BaseViewModel() {


    private val shouldMap = saveState.get<String>("shouldMap").toBoolean()
    val roleListResultFlow = resultStateFlow<RegistrationRoleResponse>()
    val selectedRole = mutableStateOf<RegistrationRole?>(null)
    var mapRole: RegistrationRoleUser? = null


    init {

        if (shouldMap) mapRole = RegistrationRoleUser(
            id = 1,
            userDetails = "Excel One Stop Solution Pvt. Ltd.(A 1)",
            mobile = "+91-925113333",
            relationId = appPreference.user?.id!!
        )

        fetchRoles()
    }

    private fun fetchRoles() {
        callApiForStateFlow(roleListResultFlow) { repository.fetchCreateRole() }
    }
}