package com.a2z.app.ui.screen.auth.registration.register_map

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.auth.RegistrationRole
import com.a2z.app.data.model.auth.RegistrationRoleResponse
import com.a2z.app.data.model.auth.RegistrationRoleUser
import com.a2z.app.data.model.auth.RegistrationRoleUserResponse
import com.a2z.app.data.repository.RegistrationRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.PagingState
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.AppConstant
import com.a2z.app.util.AppUtil
import com.a2z.app.util.resultShareFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class RegistrationMappedViewModel @Inject constructor(
    private val repository: RegistrationRepository,
    val appPreference: AppPreference,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    var job: Job? = null

    private val createRole = savedStateHandle.get<String>("createRole")?.toInt() ?: 0

    var isFetchDistributor = false
    var userType by mutableStateOf<UserType?>(null)
    var userMapperType by mutableStateOf<UserType?>(null)
    lateinit var createUserType: UserType
    val userIds = arrayListOf<String>()

    var titleObs = mutableStateOf("")
    var inputMode = ""
    var inputText = ""
    var fosId = 1

    var pagingState by mutableStateOf<PagingState<RegistrationRoleUser>>(PagingState())
    private val _registrationMappedUserResultFlow = resultShareFlow<RegistrationRoleUserResponse>()

    val bottomComponentVisibility = mutableStateOf(false)

    val confirmActionDialog = mutableStateOf(false)
    var confirmItem: RegistrationRoleUser? = null
    var confirmDescription = mutableStateOf("")

    init {

        AppUtil.logger("create role id test : $createRole")

        setUserMapperType()
        setTitle()
        setupCreateRole()


        _registrationMappedUserResultFlow.getLatest(
            progress = {
                pagingState = pagingState.loadingState()
                Unit
            },
            success = {
                pagingState = pagingState.successState(it.members!!)
            },
            failure = {
                pagingState = pagingState.failureState(it)
                Unit
            }
        )
        fetchUsers()
    }

    fun fetchUsers() {

        var userId = if (userIds.isEmpty()) "" else userIds.last()
        if (isFetchDistributor) {
            userId = appPreference.user?.id.toString()
        }

        bottomComponentVisibility.value = false

        val param = hashMapOf(
            "user_id" to userId,
            "searchType" to inputMode,
            "searchInput" to inputText,
            "distType" to if (isFetchDistributor) "FOS_DIS" else ""
        )

        callApiForShareFlow(_registrationMappedUserResultFlow,
            jobCallback = {
                this.job = it
            }) {
            repository.fetchMappingUserList(
                setUrl(),
                param
            )
        }

    }

    private fun initialUrl() =
        when (userMapperType) {
            UserType.RM -> AppConstant.BASE_URL + "sales/asm-list"
            UserType.ASM -> AppConstant.BASE_URL + "sales/fos-list"
            UserType.FOS -> AppConstant.BASE_URL + "sales/md-list"
            else -> ""
        }


    private fun setUrl() =
        if (userMapperType == userType) {
            initialUrl()
        } else when (userType) {
            UserType.ASM -> AppConstant.BASE_URL + "sales/fos"
            UserType.FOS -> AppConstant.BASE_URL + "sales/md"
            UserType.MD -> AppConstant.BASE_URL + "sales/dist"
            else -> ""
        }

    fun setUserType(isReverse: Boolean = false) {
        if (isReverse) {
            userType = when (userType) {
                UserType.Distributor -> UserType.MD
                UserType.MD -> {
                    fosId = 1
                    UserType.FOS

                }
                UserType.FOS -> UserType.ASM
                UserType.ASM -> UserType.RM
                else -> null
            }
        } else {
            userType = when (userType) {
                UserType.RM -> UserType.ASM
                UserType.ASM -> UserType.FOS
                UserType.FOS -> UserType.MD
                UserType.MD -> UserType.Distributor
                else -> null
            }
        }
        setTitle()
    }

    private fun setTitle() {
        val title = when (userType) {
            UserType.RM -> "ASM List"
            UserType.ASM -> "FOS List"
            UserType.FOS -> "MD List"
            UserType.MD -> "Distributor List"
            else -> ""
        }
        titleObs.value = title
    }


    private fun setUserMapperType() {
        userMapperType = when (appPreference.user?.roleId) {
            22 -> UserType.RM
            23 -> UserType.ASM
            24 -> UserType.FOS
            else -> null
        }
        userType = userMapperType
    }

    private fun setupCreateRole() {
        when (createRole) {
            3 -> createUserType = UserType.MD
            4 -> createUserType = UserType.Distributor
            5 -> createUserType = UserType.Retailer
        }
    }

    fun onItemClick(item: RegistrationRoleUser) {

        if (titleObs.value == "FOS List" && item.id != 1) {
            fosId = item.id
        }
        if (userMapperType == UserType.FOS) {
            fosId = appPreference.user?.roleId ?: 0
        }


        if (item.id == 1) {
            showConfirmDialog("Are you sure to mapped under direct to company", item)
        } else if (createUserType == UserType.MD
            && userType == UserType.ASM
        ) {
            showConfirmDialog("Are you sure to mapped under (${item.userDetails})", item)
        } else if (createUserType == UserType.Distributor
            && userType == UserType.FOS
        ) {
            showConfirmDialog("Are you sure to mapped under (${item.userDetails})", item)
        } else if (createUserType == UserType.Retailer
            && userType == UserType.MD
        ) {
            showConfirmDialog("Are you sure to mapped under (${item.userDetails})", item)
        } else {
            setUserType()
            userIds.add(item.id.toString())
            inputMode = ""
            inputText = ""
            pagingState.refresh()
            fetchUsers()
        }
    }

    private fun showConfirmDialog(message: String, item: RegistrationRoleUser) {

        item.relationId = fosId
        if (createRole == 3) item.id = 1
        confirmItem = item
        confirmDescription.value = message
        confirmActionDialog.value = true

    }

    enum class UserType {
        RM, ASM, FOS, MD, Distributor, Retailer
    }
}