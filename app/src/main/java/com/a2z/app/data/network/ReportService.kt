package com.a2z.app.data.network

import com.a2z.app.data.model.AppResponse
import com.a2z.app.data.model.dmt.TransactionDetail
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.data.model.report.*
import com.a2z.app.util.FieldMapData
import retrofit2.http.*

interface ReportService {

    @GET("report/ledger")
    suspend fun ledgerReport(@QueryMap data: FieldMapData): LedgerReportResponse

    @POST("check-status")
    @FormUrlEncoded
    suspend fun checkStatus(@FieldMap data: FieldMapData): AppResponse


    @POST("aeps/three/checkstatus")
    @FormUrlEncoded
    suspend fun aepsCheckStatus(@FieldMap data: FieldMapData): AppResponse


    @GET
    suspend fun aepsPrintDetail(@Url data: String): TransactionDetailResponse


    @POST("complain/store")
    @FormUrlEncoded
    suspend fun makeComplain(@FieldMap data: FieldMapData): AppResponse

    @GET
    suspend fun downloadLedgerReceiptData(@Url url: String): TransactionDetailResponse

    @GET("user/scheme/list")
    suspend fun schemeList(): CommissionSchemeListResponse


    @GET("user/scheme")
    suspend fun schemeDetail(@QueryMap data: FieldMapData): CommissionSchemeDetailResponse

    @GET("complain/remark/list")
    suspend fun fetchComplainTypes(@QueryMap data: FieldMapData): ComplainTypeListResponse


    @GET("matm/report")
    suspend fun matmRequestReport(@QueryMap data: FieldMapData): MatmReportResponse


    @GET("aeps/report")
    suspend fun aepsRequestReport(@QueryMap data: FieldMapData): AepsReportResponse


    @GET("fund-report")
    suspend fun fundReport(@QueryMap data: FieldMapData): FundReportResponse


    @GET("get-direct-fund-transfer")
    suspend fun dtReport(@QueryMap data: FieldMapData): DTReportResponse


}
