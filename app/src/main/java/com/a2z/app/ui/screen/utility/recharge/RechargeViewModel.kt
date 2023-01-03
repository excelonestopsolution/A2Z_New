package com.a2z.app.ui.screen.utility.recharge

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.provider.Operator
import com.a2z.app.data.model.utility.RechargeDthInfo
import com.a2z.app.data.model.utility.RechargeOffer
import com.a2z.app.data.model.utility.RechargeOfferResponse
import com.a2z.app.data.model.utility.RechargeTransactionResponse
import com.a2z.app.data.repository.UtilityRepository
import com.a2z.app.ui.screen.utility.util.OperatorType
import com.a2z.app.ui.screen.utility.util.RechargeUtil
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.InputWrapper
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeParcelable
import com.a2z.app.ui.util.extension.safeSerializable
import com.a2z.app.ui.util.resource.BannerType
import com.a2z.app.ui.util.resource.ResultType
import com.a2z.app.ui.util.resource.StatusDialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val repository: UtilityRepository,
    savedStateHandle: SavedStateHandle,
    appPreference: AppPreference
) : BaseViewModel() {

    val operator: Operator = savedStateHandle.safeParcelable("operator")!!
    private var operatorType: OperatorType = savedStateHandle.safeSerializable("operatorType")!!

    val util = RechargeUtil(
        appPreference = appPreference,
        operator = operator,
        operatorType = operatorType,
    )

    val inputMaxLength = util.getInputMaxLength()
    val inputMinAmount = util.getInputMinAmount()
    val inputDownText = util.getNumberDownText()


    val input = RechargeInput(
        inputValidator = { util.rechargeInputValidator(it) },
        amountValidator = { util.rechargeAmountValidator(it) }
    )


    var rOfferDialogState = mutableStateOf(false)
    var rOfferState = mutableStateOf<RechargeOffer?>(null)
    var confirmDialogState = mutableStateOf(false)
    private var rOfferList: List<RechargeOffer>? = null

    fun onNumberChange(it: String) {
        if (isPrepaid()) {
            rOfferList = null
            if (it.length == 10) fetchROffer()
        }

    }

    fun onAmountChange(amount: String) {
        if (isPrepaid()) {
            if (rOfferList == null) return
            rOfferState.value = rOfferList!!.find {
                it.price.trim() == amount
            }
        }
    }


    private val _rOfferFlow =
        MutableStateFlow<ResultType<RechargeOfferResponse>>(ResultType.Loading())
    val rOfferFlow: StateFlow<ResultType<RechargeOfferResponse>> = _rOfferFlow
    private fun fetchROffer() {
        callApiForStateFlow(_rOfferFlow, beforeEmit = {
            if (it is ResultType.Success) {
                if (it.data.status.equals("success", ignoreCase = true)) {
                    rOfferList = it.data.offers
                }
            }
        }) {
            repository.fetchRechargeOffer(
                "mobile_number" to input.numberInputWrapper.input.value,
                "provider" to operator.operatorName.toString()
            )
        }
    }

    val dthInfoState = mutableStateOf<RechargeDthInfo?>(null)
    private fun fetchDthInfo() {


        callApiForStateFlow(beforeEmit = {

            when (it) {
                is ResultType.Loading -> {
                    dthInfoState.value = null
                    dialogState.value = StatusDialogType.Progress()
                }
                is ResultType.Success -> {
                    dialogState.value = StatusDialogType.None
                    if (it.data.status == 1) {
                        dthInfoState.value = it.data.info
                        bannerState.value = BannerType.Success(
                            title = "Dth Account Info",
                            message = it.data.message
                        )
                    } else {
                        bannerState.value = BannerType.Failure(
                            title = "Dth Account Info",
                            message = it.data.message
                        )
                    }
                }
                is ResultType.Failure -> {
                    dialogState.value = StatusDialogType.None
                }
            }
        }) {
            repository.fetchDthInfo(
                "acc_no" to input.numberInputWrapper.getValue(),
                "provider_id" to operator.id.toString(),
            )
        }
    }


    fun rechargeTransaction(): MutableSharedFlow<ResultType<RechargeTransactionResponse>> {

        val number = input.numberInputWrapper.input.value
        val providerId = operator.id.toString()
        val amount = input.amountInputWrapper.input.value

        return callApiForShareFlow {
            repository.rechargeTransaction(
                "number" to number,
                "provider" to providerId,
                "amount" to amount,
            )
        }
    }

    fun isPrepaid() = operatorType == OperatorType.PREPAID

    fun onFetchInfoButtonClick() {
        if (isPrepaid()) rOfferDialogState.value = true
        else fetchDthInfo()
    }

    fun getFetchInfoButtonText(): String {
        return if (isPrepaid()) "R-Offer" else "Fetch Info"
    }

}


data class RechargeInput(
    //validators
    private var inputValidator: (String) -> Pair<Boolean, String>,
    private var amountValidator: (String) -> Pair<Boolean, String>,

    //inputs
    val numberInputWrapper: InputWrapper = InputWrapper { inputValidator(it) },
    val amountInputWrapper: InputWrapper = InputWrapper { amountValidator(it) }

) : BaseInput(
    numberInputWrapper, amountInputWrapper
)