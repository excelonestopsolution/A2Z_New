package com.a2z.app.data.repository

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.app.BalanceResponse
import com.a2z.app.data.model.app.BannerResponse
import com.a2z.app.data.model.app.NewsResponse
import com.a2z.app.data.model.app.QRCodeResponse
import com.a2z.app.data.model.member.MemberListResponse
import com.a2z.app.data.model.member.RegisterCompleteUserResponse
import com.a2z.app.data.model.member.RegisterInCompleteUserResponse
import com.a2z.app.data.model.settlement.SettlementAddedBankListResponse
import com.a2z.app.data.model.settlement.SettlementBankListResponse
import com.a2z.app.ui.screen.members.list.MemberListScreen
import com.a2z.app.util.FieldMapData
import retrofit2.http.QueryMap

interface MemberRepository {
    suspend fun memberList(data : FieldMapData): MemberListResponse
    suspend fun fundTransferMemberList(data: FieldMapData): MemberListResponse

    suspend fun saleMembersList(url : String,data: FieldMapData) : MemberListResponse

    suspend fun fetchCompletedUserList(data: FieldMapData) : RegisterCompleteUserResponse

    suspend fun fetchInCompletedUserList(data: FieldMapData) : RegisterInCompleteUserResponse

}