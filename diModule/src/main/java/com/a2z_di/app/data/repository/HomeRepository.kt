package com.a2z_di.app.data.repository

import com.a2z_di.app.data.service.HomeService
import javax.inject.Inject

class HomeRepository @Inject constructor (private val homeService : HomeService){

    suspend fun fetchBalanceInfo() = homeService.fetchBalanceInfo()
    suspend fun fetchQurCodeData() = homeService.fetchQurCodeData()
    suspend fun kycCheck() = homeService.kycCheck()
    suspend fun fetchBanner() = homeService.fetchBanner()
    suspend fun fetchNews(userId : String, token : String) = homeService.fetchNews(userId,token)
    suspend fun bankDown()  = homeService.bankDown()
}