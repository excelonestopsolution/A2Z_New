package com.a2z.app.ui.screen.home.sale

import androidx.lifecycle.SavedStateHandle
import com.a2z.app.data.local.AppPreference
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.screen.members.list.MemberListType
import com.a2z.app.ui.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaleHomeViewModel @Inject constructor(
    appPreference: AppPreference
) : BaseViewModel() {

    val user = appPreference.user

    fun getMappedUserTags(): List<String> {
        return when (user?.roleId) {
            22 -> arrayListOf("ASM", "API", "FOS", "MD", "Distributor", "Retailer")
            23 -> arrayListOf("FOS", "MD", "Distributor", "Retailer")
            24 -> arrayListOf("MD", "Distributor", "Retailer")
            else -> emptyList()
        }
    }

    fun navigateMemberList(searchFor: String) {

        val memberType = when (searchFor) {
            "ASM" -> MemberListType.ASM
            "API" -> MemberListType.API
            "FOS" -> MemberListType.FOS
            "MD" -> MemberListType.MD
            "Distributor" -> MemberListType.DISTRIBUTOR
            "Retailer" -> MemberListType.RETAILER
            else -> MemberListType.NA
        }

        navigateTo(NavScreen.MemberListScreen.passArgs(
            isTransfer = false,
            memberType = memberType,
            isSale = true
        ))
    }
}