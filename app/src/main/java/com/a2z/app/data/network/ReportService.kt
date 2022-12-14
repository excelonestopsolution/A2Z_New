package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeDetailResponse
import com.a2z.app.data.model.report.CommissionSchemeListResponse
import com.a2z.app.data.model.report.ComplainTypeListResponse
import com.a2z.app.data.model.report.LedgerReportResponse
import com.a2z.app.util.FieldMapData
import retrofit2.Response
import retrofit2.http.*

interface ReportService {

    @GET("report/ledger")
    suspend fun ledgerReport(@QueryMap data : FieldMapData) : LedgerReportResponse

    @POST("check-status")
    @FormUrlEncoded
    suspend fun checkStatus(@FieldMap data : FieldMapData) : AppResponse

    @POST("complain/store")
    @FormUrlEncoded
    suspend fun makeComplain(@FieldMap data : FieldMapData) : AppResponse

    @GET
    suspend fun downloadLedgerReceiptData(@Url url : String) : TransactionDetailResponse

    @GET("user/scheme/list")
    suspend fun schemeList() : CommissionSchemeListResponse


    @GET("user/scheme")
    suspend fun schemeDetail(@QueryMap data : FieldMapData) : CommissionSchemeDetailResponse

    @GET("complain/remark/list")
    suspend fun fetchComplainTypes(@QueryMap data : FieldMapData) : ComplainTypeListResponse



}
