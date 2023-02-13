package com.a2z.app.data.repository_impl

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.aeps.AepsBankListResponse
import com.a2z.app.data.model.aeps.AepsTransaction
import com.a2z.app.data.model.settlement.SettlementAddedBankListResponse
import com.a2z.app.data.model.settlement.SettlementBankListResponse
import com.a2z.app.data.network.AepsService
import com.a2z.app.data.network.MemberService
import com.a2z.app.data.repository.AepsRepository
import com.a2z.app.data.repository.MemberRepository
import com.a2z.app.util.FieldMapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRepositoryImpl @Inject constructor(private val service: MemberService) :
    MemberRepository {
    override suspend fun memberList(data : FieldMapData) = service.memberList(data)

}