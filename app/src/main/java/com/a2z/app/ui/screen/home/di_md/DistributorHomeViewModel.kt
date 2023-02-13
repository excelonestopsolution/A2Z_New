package com.a2z.app.ui.screen.home.di_md

import androidx.compose.runtime.mutableStateOf
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.report.AgentRequestView
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DistributorHomeViewModel @Inject constructor(
    private val repository: ReportRepository,
    private val transactionRepository: TransactionRepository
) : BaseViewModel() {

    val reportResultFlow = resultStateFlow<Any>()
    val approveDialogState = mutableStateOf(false)
    val agentRequestView = mutableStateOf<AgentRequestView?>(null)

    private val _approveResultFlow = resultShareFlow<AppResponse>()

    init {
        fetchReport()

        _approveResultFlow.getLatest {
            when (it.status) {
                1 -> successDialog(it.message){
                    fetchReport()
                }
                else -> alertDialog(it.message)
            }
        }
    }

    private fun fetchReport() {
        callApiForStateFlow(reportResultFlow) { repository.agentRequestView(hashMapOf()) }
    }

    fun onProceed(remark: String, status: String, remarkInput: String) {

        if (status == agentRequestView.value?.status_id.toString()) {
            alertDialog("Same status can't be proceed!")
            return
        }

        val param = hashMapOf(
            "id" to agentRequestView.value?.id.toString(),
            "status_id" to status,
            "remark" to remark,
            "adminRemark" to remark,
            "approve_remark" to remarkInput,

            )
        callApiForShareFlow(
            _approveResultFlow
        ) { transactionRepository.approveFundRequest(param) }

    }

}