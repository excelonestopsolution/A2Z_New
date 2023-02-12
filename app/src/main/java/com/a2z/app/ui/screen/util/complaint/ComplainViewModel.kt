package com.a2z.app.ui.screen.util.complaint

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.a2z.app.data.model.report.ComplainListResponse
import com.a2z.app.data.model.report.Complaint
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.PagingState
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ComplainViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {


    private val _reportPagingFlow = resultStateFlow<ComplainListResponse>()
    var pagingState by mutableStateOf<PagingState<Complaint>>(PagingState())

    init {
        fetchReport()

        _reportPagingFlow.getLatest(
            progress = {
                pagingState = pagingState.loadingState()
            },
            success = {
                pagingState = if (it.status == 1)
                    pagingState.successState(it.complains!!)
                else pagingState.failureState(Exception(it.message.toString()))
            },
            failure = {
                pagingState = pagingState.failureState(it)
            }
        )
    }

    fun fetchReport() {

        val param = hashMapOf("page" to pagingState.page.toString(),)
        callApiForShareFlow(
            flow = _reportPagingFlow,
            call = { repository.complaintRequestView(param) }
        )
    }
}