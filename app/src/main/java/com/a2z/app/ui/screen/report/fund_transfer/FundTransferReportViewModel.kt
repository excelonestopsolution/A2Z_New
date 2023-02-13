package com.a2z.app.ui.screen.report.fund_transfer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.a2z.app.data.model.report.*
import com.a2z.app.data.repository.ReportRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.PagingState
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.*
import com.a2z.app.util.extension.insertDateSeparator
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FundTransferReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : BaseViewModel() {

    var searchInput by mutableStateOf(SearchInput())

    var userList = mutableListOf<Pair<String,String>>()

    var pagingState by mutableStateOf<PagingState<FundTransferReport>>(PagingState())
    val filterDialogVisibleState = mutableStateOf(value = false)
    private val _reportPagingFlow = resultStateFlow<Any>()


    init {

        fetchReport()

        _reportPagingFlow.getLatest(


            progress = {
                pagingState = pagingState.loadingState()
                Unit
            },
            success = {


                val mainResponse = Gson().toJson(it)
                val usersObject = JSONObject(mainResponse).getJSONObject("users")
                val userKeys = usersObject.keys()

                val mList = arrayListOf<Pair<String,String>>()
                mList.add(Pair("All",""))
                userKeys.forEach {key->
                    val value = usersObject.getString(key)
                    mList.add(Pair(value, key))
                }

                userList.clear()
                userList.addAll(mList)

                val reportResponse = Gson().fromJson(mainResponse,FundTransferReportResponse::class.java)

                pagingState = if (reportResponse.status == 1)
                    pagingState.successState(reportResponse.reports!!,)
                else pagingState.failureState(Exception(reportResponse.message.toString()))
            },
            failure = {
                pagingState = pagingState.failureState(it)
                Unit
            }
        )
    }


    fun fetchReport() {
        val param = searchInput.run {
            hashMapOf(
                "todate" to this.endDate.insertDateSeparator(),
                "fromdate" to this.startDate.insertDateSeparator(),
                "user" to this.user.second.toString(),
                "page" to pagingState.page.toString(),
            )
        }
        callApiForShareFlow(
            flow = _reportPagingFlow,
            call = { repository.fundTransfer(param) }
        )
    }

    data class SearchInput(
        val startDate: String = DateUtil.getDate(),
        val endDate: String = DateUtil.getDate(),
        val user : Pair<String,String> = Pair("","")
    )

}