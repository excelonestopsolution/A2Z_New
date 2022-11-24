package com.a2z.app.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.repository.AppRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForFlow
import com.a2z.app.ui.util.resource.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



val initialBalanceFetched = mutableStateOf(false)

@HiltViewModel
open class AppViewModel @Inject constructor(
    private val repository: AppRepository,
    val appPreference: AppPreference
) : BaseViewModel() {

    private var showProgress = true

    val balanceFlow = MutableStateFlow<ResultType<BalanceResponse>>(ResultType.Loading())
    val balanceObs = mutableStateOf(appPreference.user?.userBalance)


    init {
        fetchWalletBalance(false)

        viewModelScope.launch {
            balanceFlow.getLatest(progress = {
               if(initialBalanceFetched.value) if (showProgress) progressDialog() }
            ) {
                if (!initialBalanceFetched.value) return@getLatest
                if (it.status == 1) {
                    if (showProgress)
                        successBanner("Wallet Balance", "Balance updated successfully!")
                    balanceObs.value = it.balance.userBalance
                    val user = appPreference.user!!
                    appPreference.user = user.apply { this.userBalance = balanceObs.value!! }
                } else failureDialog(it.message)
            }
        }
}

    fun fetchWalletBalance(progress: Boolean = true) {
        this.showProgress = progress
        callApiForFlow(balanceFlow) { repository.fetchWalletBalance() }
    }

}
