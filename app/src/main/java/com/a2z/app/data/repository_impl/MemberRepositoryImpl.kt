package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.member.MemberListResponse
import com.a2z.app.data.model.member.RegisterCompleteUserResponse
import com.a2z.app.data.model.member.RegisterInCompleteUserResponse
import com.a2z.app.data.network.MemberService
import com.a2z.app.data.repository.MemberRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRepositoryImpl @Inject constructor(private val service: MemberService) :
    MemberRepository {
    override suspend fun memberList(data : FieldMapData) = service.memberList(data)
    override suspend fun fundTransferMemberList(data: FieldMapData) = service.fundTransferMemberList(data)
    override suspend fun saleMembersList(url : String,data: FieldMapData) = service.saleMembersList(url,data)
    override suspend fun fetchCompletedUserList(data: FieldMapData) = service.fetchCompletedUserList(data)
    override suspend fun fetchInCompletedUserList(data: FieldMapData) = service.fetchInCompletedUserList(data)


}