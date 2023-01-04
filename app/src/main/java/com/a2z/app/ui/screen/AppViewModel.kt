package com.a2z.app.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.resource.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
open class AppViewModel @Inject constructor(
    private val repository: AppRepository,
    val appPreference: AppPreference
) : BaseViewModel() {

    private val _balanceFlow = MutableSharedFlow<ResultType<BalanceResponse>>()
    val balanceObs = mutableStateOf(appPreference.user?.userBalance)


    init {
        viewModelScope.launch {
            _balanceFlow.getLatest {
                if (it.status == 1) {
                    successBanner("Wallet Balance", "Balance updated successfully!")
                    balanceObs.value = it.balance.userBalance
                    val user = appPreference.user!!
                    appPreference.user = user.apply { this.userBalance = balanceObs.value!! }
                } else failureDialog(it.message)
            }
        }
    }

    fun fetchWalletBalance() {
        callApiForShareFlow(_balanceFlow, popUpScreen = false) { repository.fetchWalletBalance() }
    }

}
