package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.member.MemberListResponse
import com.a2z.app.data.model.member.RegisterCompleteUserResponse
import com.a2z.app.data.model.member.RegisterInCompleteUserResponse
import com.a2z.app.data.model.settlement.SettlementAddedBankListResponse
import com.a2z.app.data.model.settlement.SettlementBankListResponse
import com.a2z.app.util.FieldMapData
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface MemberService {

    @GET("retailer-fund-transfer")
    suspend fun fundTransferMemberList(@QueryMap data: FieldMapData): MemberListResponse

    @GET("get-members")
    suspend fun memberList(@QueryMap data: FieldMapData): MemberListResponse

    @GET
    suspend fun saleMembersList(@Url url: String, @QueryMap data: FieldMapData): MemberListResponse

    @GET("sales/created/member")
    suspend fun fetchCompletedUserList(
        @QueryMap data: FieldMapData
    ): RegisterCompleteUserResponse

    @GET("user/incomplete/registration")
    suspend fun fetchInCompletedUserList(
        @QueryMap data: FieldMapData
    ): RegisterInCompleteUserResponse
}
