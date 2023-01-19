package com.a2z.app.ui.screen.utility.operator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.model.provider.Operator
import com.a2z.app.data.model.provider.OperatorResponse
import com.a2z.app.data.repository.UtilityRepository
import com.a2z.app.ui.screen.utility.util.OperatorType
import com.a2z.app.ui.screen.utility.util.UtilityUtil
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeSerializable
import com.a2z.app.ui.util.resource.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OperatorViewModel @Inject constructor(
    private val utilityRepository: UtilityRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    val operatorType: OperatorType = savedStateHandle.safeSerializable("operatorType")!!

    var operatorList: List<Operator> = emptyList()
    var operatorListObs = mutableListOf<Operator>()
    var spinnerDialogState = mutableStateOf(false)
    var selectedState = mutableStateOf("RAJASTHAN")

    val util = UtilityUtil(operatorType = operatorType)

    private val _providerResponseObs =
        MutableStateFlow<ResultType<OperatorResponse>>(ResultType.Loading())
    val providerResponseObs = _providerResponseObs.asStateFlow()

    init {
        fetchOperatorList()
    }

    private fun getRequestTypeFromOperator(): String {
        return when (operatorType) {
            OperatorType.PREPAID -> "MOBILE_PREPAID"
            OperatorType.POSTPAID -> "POSTPAID"
            OperatorType.DTH -> "DTH"
            OperatorType.FAS_TAG -> "FASTTAG"
            OperatorType.ELECTRICITY -> "ELECTRICITY"
            OperatorType.WATER -> "WATER"
            OperatorType.GAS -> "GAS"
            OperatorType.INSURANCE -> "INSURANCE"
            OperatorType.LOAN_REPAYMENT -> "LOAN_REPAYMENT"
            OperatorType.BROADBAND -> "BROADBAND"
        }
    }

    private fun fetchOperatorList(stateId: String = "") {
        val requestType = getRequestTypeFromOperator()
        callApiForShareFlow (
            flow = _providerResponseObs){
            utilityRepository.rechargeOperators(
                "requestType" to requestType,
                "state_id" to stateId
            )
        }
    }

    val searchValue = mutableStateOf("")

    fun querySearch(it: String) {
        searchValue.value = it
        val newSearchList = operatorList.filter { operator ->
            operator.operatorName!!.contains(it, ignoreCase = true)
        }
        operatorListObs.clear()
        operatorListObs.addAll(newSearchList)
    }

    fun clearSearch() {
        searchValue.value = ""
        operatorListObs.clear()
        operatorListObs.addAll(operatorList)
    }

    fun getStateList() = listOf(
        "2" to "HIMACHAL PRADESH",
        "3" to "PUNJAB",
        "5" to "UTTARAKHAND",
        "6" to "HARYANA",
        "7" to "DELHI",
        "8" to "RAJASTHAN",
        "9" to "UTTAR PRADESH",
        "10" to "BIHAR",
        "13" to "NAGALAND",
        "14" to "MANIPUR",
        "16" to "TRIPURA",
        "17" to "MEGHALAYA",
        "18" to "ASSAM",
        "19" to "WEST BENGAL",
        "20" to "JHARKHAND",
        "21" to "Odisha",
        "22" to "CHHATTISGARH",
        "23" to "MADHYA PRADESH",
        "24" to "GUJARAT",
        "25" to "DAMAN AND DIU",
        "27" to "MAHARASHTRA",
        "28" to "ANDHRA PRADESH",
        "29" to "KARNATAKA",
        "32" to "KERALA",
        "33" to "TAMIL NADU"
    )

    fun onStateSelect(state: String) {
        if (selectedState.value != state) {
            selectedState.value = state
            val stateId = getStateList().find {
                it.second == state
            }!!.first
            fetchOperatorList(stateId = stateId)
        }
    }
}