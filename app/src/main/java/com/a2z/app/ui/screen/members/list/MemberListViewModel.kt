package com.a2z.app.ui.screen.members.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.a2z.app.data.local.AppPreference
import com.a2z.app.data.model.member.Member
import com.a2z.app.data.model.member.MemberListResponse
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.data.model.report.LedgerReportResponse
import com.a2z.app.data.repository.MemberRepository
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.AppPagingConfig
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.PagingState
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.callApiForStateFlow
import com.a2z.app.util.extension.insertDateSeparator
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MemberListViewModel @Inject constructor(
    private val appPreference: AppPreference,
    private val repository: MemberRepository
) : BaseViewModel() {

    private val _memberResponseFlow = resultStateFlow<MemberListResponse>()
    val showFilterDialogState = mutableStateOf(false)
    var pagingState by mutableStateOf<PagingState<Member>>(PagingState(
        pagingConfig = AppPagingConfig(
            maxPage = 30,
            listSize = 40
        )
    ))


    init {
        fetchMembers()

        _memberResponseFlow.getLatest(
            progress = {
                pagingState = pagingState.loadingState()
                Unit
            },
            success = {
                pagingState = if (it.status == 1)
                    pagingState.successState(it.members!!)
                else pagingState.failureState(Exception(it.message.toString()))
            },
            failure = {
                pagingState = pagingState.failureState(it)
                Unit
            }
        )

    }

    fun fetchMembers(
        searchType : String? =null,
        searchInput : String? =null,
    ){
        val param =
            hashMapOf(
                "type" to "",
                "page" to pagingState.page.toString(),
                "searchType" to searchType.orEmpty(),
                "searchInput" to searchInput.orEmpty(),
            )

        callApiForShareFlow(
            flow = _memberResponseFlow,
            call = { repository.memberList(param) }
        )
    }
}