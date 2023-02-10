package com.a2z.app.dist.data.service

import com.a2z.app.model.CommonResponse
import com.a2z.app.model.TransactionDetailResponse
import com.a2z.app.model.report.*
import com.a2z.app.util.ents.FieldMapData
import retrofit2.Response
import retrofit2.http.*

interface ReportService {

    @GET("report/ledger")
    suspend fun ledgerReport(@QueryMap data : FieldMapData) : LedgerReportResponse

    @POST("check-status")
    @FormUrlEncoded
    suspend fun checkStatus(@FieldMap data : FieldMapData) : CommonResponse

    @POST("complain/store")
    @FormUrlEncoded
    suspend fun makeComplain(@FieldMap data : FieldMapData) : CommonResponse

    @GET
    suspend fun downloadLedgerReceiptData(@Url url : String) : Response<TransactionDetailResponse>

    @GET("complain/remark/list")
    suspend fun fetchComplainTypes(@QueryMap data : FieldMapData) : ComplainTypeListResponse



}