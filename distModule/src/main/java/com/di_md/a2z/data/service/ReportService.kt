package com.di_md.a2z.data.service

import com.di_md.a2z.data.model.TransactionDetailResponse
import com.di_md.a2z.data.model.report.*
import com.di_md.a2z.data.response.CommonResponse
import com.di_md.a2z.util.ents.FieldMapData
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