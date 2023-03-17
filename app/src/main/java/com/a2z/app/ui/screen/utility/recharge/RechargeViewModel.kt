package com.a2z.app.ui.screen.utility.recharge

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.provider.Operator
import com.a2z.app.data.model.utility.*
import com.a2z.app.data.repository.TransactionRepository
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
import com.a2z.app.util.AppUtil
import com.a2z.app.util.extension.notNullOrEmpty
import com.a2z.app.util.extension.nullOrEmptyToDouble
import com.a2z.app.util.resultStateFlow
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val repository: UtilityRepository,
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle,
    appPreference: AppPreference
) : BaseViewModel() {

    val countryState: Pair<String, String> = savedStateHandle.safeSerializable("countryState")!!

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
    var rechargePlanDialogState = mutableStateOf(false)
    var rechargePlanState = mutableStateOf<RechargePlan?>(null)
    var confirmDialogState = mutableStateOf(false)
    var offerList: List<RechargePlan> = emptyList()

    fun onNumberChange(it: String) {
        if (isPrepaid()) {
            offerList = emptyList()
            if (it.length == 10) {
                fetchRechargePlan()
                fetchROffer()
            } else {
                rechargePlanState.value = null
                input.amountInputWrapper.setValue("")
                input.amountInputWrapper.clearFormError()
            }
        }

    }

    fun onAmountChange(amount: String) {

        if (isPrepaid()) {
            if (offerList.isEmpty()) return
            rechargePlanState.value = offerList.find {
                it.rs.nullOrEmptyToDouble() == amount.nullOrEmptyToDouble()
            }
        }
    }


    val rOfferFlow = resultStateFlow<ROfferResponse>()
    private fun fetchROffer() {

        val rOfferList = offerList.filter { it.isROffer }
        if (rOfferList.isEmpty())
            callApiForStateFlow(rOfferFlow, beforeEmit = {
                if (it is ResultType.Success) {
                    if (it.data.status.equals("success", ignoreCase = true)) {
                        offerList.plus(it.data.offers.map { e ->
                            e.apply {
                                this.isROffer = true
                            }
                        })
                    }
                }
            }) {
                repository.fetchRechargeOffer(
                    "mobile_number" to input.numberInputWrapper.input.value,
                    "provider" to operator.operatorName.toString()
                )
            }
    }


    val rechargePlanResponseFlow = resultStateFlow<Any>()
    private fun fetchRechargePlan() {
        val simpleList = offerList.filter { !it.isROffer }
        if (simpleList.isEmpty())
            callApiForStateFlow(rechargePlanResponseFlow, beforeEmit = {
                if (it is ResultType.Success) {
                    val rechargePlans = arrayListOf<RechargePlan>()

                    val response = JSONObject(Gson().toJson(it.data))
                    val status = response.getInt("status")
                    if (status == 1) {
                        val records = response.getJSONObject("records")
                        val keys = records.keys()
                        keys.forEach { key ->
                            val mArray = records.getJSONArray(key)
                            for (i in 0 until mArray.length()) {
                                rechargePlans.add(
                                    RechargePlan(
                                        rs = mArray.getJSONObject(i).optString("rs"),
                                        desc = mArray.getJSONObject(i).optString("desc"),
                                        validity = mArray.getJSONObject(i).optString("validity"),
                                        remark = mArray.getJSONObject(i).optString("remark"),
                                        discontinued = mArray.getJSONObject(i)
                                            .optString("discontinued"),
                                    )
                                )

                            }
                        }
                        offerList.plus(rechargePlans)
                    }


                }
            }) {
                repository.fetchRechargePlan(
                    "state" to countryState.first,
                    "provider" to operator.id.toString()
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
            transactionRepository.rechargeTransaction(
                hashMapOf(
                    "number" to number,
                    "provider" to providerId,
                    "amount" to amount,
                    "state" to countryState.first
                )
            )
        }
    }

    fun isPrepaid() = operatorType == OperatorType.PREPAID

    fun onFetchInfoButtonClick() {
        if (isPrepaid()) rechargePlanDialogState.value = true
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