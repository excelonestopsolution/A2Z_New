package com.a2z.app.ui.screen.members.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.member.Member
import com.a2z.app.data.model.member.MemberListResponse
import com.a2z.app.data.repository.MemberRepository
import com.a2z.app.data.repository.TransactionRepository
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.util.*
import com.a2z.app.ui.util.extension.callApiForShareFlow
import com.a2z.app.ui.util.extension.safeSerializable
import com.a2z.app.util.resultShareFlow
import com.a2z.app.util.resultStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MemberListViewModel @Inject constructor(
    private val repository: MemberRepository,
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val memberType: MemberListType = savedStateHandle.safeSerializable("memberType")!!
    private val isSale : Boolean = savedStateHandle.get<String>("isSale").toBoolean()
    val isTransfer = savedStateHandle.get<String?>("isTransfer")?.toBoolean()!!

    var member = mutableStateOf<Member?>(null)

    val transferFormInput = FormInput()

    val title: String
        get() {
            val suffix = if (isTransfer) " Fund Transfer" else " List"
            return when (memberType) {
                MemberListType.RETAILER -> "Retailer$suffix"
                MemberListType.DISTRIBUTOR -> "Distributor$suffix"
                MemberListType.MD -> "MD$suffix"
                MemberListType.API -> "API$suffix"
                MemberListType.FOS -> "FOS$suffix"
                MemberListType.ASM -> "ASM$suffix"
                MemberListType.NA -> "Not Available"
            }
        }

    private val paramType: String
        get() = when (memberType) {
            MemberListType.DISTRIBUTOR -> "Distributor"
            else ->""
        }

    private val _memberResponseFlow = resultStateFlow<MemberListResponse>()
    val showFilterDialogState = mutableStateOf(false)
    val showTransferDialogState = mutableStateOf(false)
    private val _fundTransferResultFlow = resultShareFlow<AppResponse>()


    var pagingState by mutableStateOf<PagingState<Member>>(
        PagingState(
            pagingConfig = AppPagingConfig(
                maxPage = 30,
                listSize = 40
            )
        )
    )


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

        _fundTransferResultFlow.getLatest {
            if (it.status == 1) successDialog(it.message)
            {
                pagingState.refresh()
                fetchMembers()
            }
            else alertDialog(it.message)
        }

    }

    fun fetchMembers(
        searchType: String? = null,
        searchInput: String? = null,
    ) {
        val param =
            hashMapOf(
                "type" to paramType,
                "page" to pagingState.page.toString(),
                "searchType" to searchType.orEmpty(),
                "SEARCH_TYPE" to searchType.orEmpty(),
                "searchInput" to searchInput.orEmpty(),
                "number" to searchInput.orEmpty(),
            )

        callApiForShareFlow(
            flow = _memberResponseFlow,
            call = {
                if(!isSale){
                    if (isTransfer) repository.fundTransferMemberList(param)
                    else repository.memberList(param)
                }
                else{
                   val url = when (memberType) {
                        MemberListType.MD -> "sales/member/md-list"
                        MemberListType.API -> "sales/member/api-list"
                        MemberListType.FOS -> "sales/member/fos-list"
                        MemberListType.ASM -> "sales/member/md-list"
                        MemberListType.DISTRIBUTOR -> "sales/member/distributor-list"
                        MemberListType.RETAILER -> "sales/member/retailer-list"
                        MemberListType.NA -> ""
                    }
                    repository.saleMembersList(url,param)
                }
            }
        )
    }

    fun onTransfer(amount: String, remark: String) {

        val param = hashMapOf(
            "agentId" to member.value?.id.toString(),
            "amount" to amount,
            "remark" to remark,
        )


        callApiForShareFlow(_fundTransferResultFlow) {
            transactionRepository.memberFundTransfer(
                param
            )
        }

    }

    data class FormInput(
        val amount: InputWrapper = InputWrapper {
            AppValidator.amountValidation(
                it,
                minAmount = 1.0
            )
        },
        val remark: InputWrapper = InputWrapper { AppValidator.empty(it) }
    ) : BaseInput(amount,remark)
}