package com.a2z.app.ui.screen.members.by_sale

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.model.member.RegisterCompleteUser
import com.a2z.app.data.model.member.RegisterCompleteUserResponse
import com.a2z.app.data.model.member.RegisterInCompleteUser
import com.a2z.app.data.model.member.RegisterInCompleteUserResponse
import com.a2z.app.data.repository.MemberRepository
import com.a2z.app.ui.util.BaseViewModel
import com.a2z.app.ui.util.PagingState
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MemberCreatedBySaleViewModel @Inject constructor(
    private val repository: MemberRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val isComplete = savedStateHandle.get<String>("isCompleted").toBoolean()
    val subTitle : String
    get() = if(isComplete) "Complete Registration" else "Incomplete Registration"

    var searchType : String = ""
    var searchInput : String = ""

    var completePagingState by mutableStateOf<PagingState<RegisterCompleteUser>>(PagingState())
    var inCompletePagingState by mutableStateOf<PagingState<RegisterInCompleteUser>>(PagingState())

    private val _completeUserListResponse = resultStateFlow<RegisterCompleteUserResponse>()
    private val _incompleteUserListResponse = resultStateFlow<RegisterInCompleteUserResponse>()

    val showFilterDialogState = mutableStateOf(false)

    val userInfoDialogState = mutableStateOf<RegisterCompleteUser?>(null)

    init {
      if(isComplete)  fetchCompletedUsers() else fetchInCompletedUsers()

        _completeUserListResponse.getLatest(
            progress = {
                completePagingState = completePagingState.loadingState()
                Unit
            },
            success = {
                completePagingState = if (it.status == 1)
                    completePagingState.successState(it.listData!!,)
                else completePagingState.failureState(Exception(it.message.toString()))
            },
            failure = {
                completePagingState = completePagingState.failureState(it)
                Unit
            }
        )
        _incompleteUserListResponse.getLatest(
            progress = {
                inCompletePagingState = inCompletePagingState.loadingState()
                Unit
            },
            success = {
                inCompletePagingState = if (it.status == 1)
                    inCompletePagingState.successState(it.members!!)
                else inCompletePagingState.failureState(Exception(it.message.toString()))
            },
            failure = {
                inCompletePagingState = inCompletePagingState.failureState(it)
                Unit
            }
        )
    }


    fun fetchCompletedUsers() {
        val param =
            hashMapOf(
                "page" to completePagingState.page.toString(),
                "searchType" to (searchType ?: ""),
                "searchInput" to (searchInput ?: ""),
            )
        callApiForShareFlow(
            flow = _completeUserListResponse,
            call = { repository.fetchCompletedUserList(param)
            }
        )
    }

    fun fetchInCompletedUsers() {
        val param =
            hashMapOf(
                "page" to completePagingState.page.toString(),
                "searchType" to (searchType ?: ""),
                "searchInput" to (searchInput ?: ""),
            )
        callApiForShareFlow(
            flow = _incompleteUserListResponse,
            call = { repository.fetchInCompletedUserList(param)
            }
        )
    }
}