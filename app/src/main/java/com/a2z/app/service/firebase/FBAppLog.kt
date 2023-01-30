package com.a2z.app.service.firebase

import com.a2z.app.util.DateUtil

data class FBAppLog(
    val logs: String,
    val apiName: String = "NA",
    val isSuccess: Boolean ,
    val createdAt: String = DateUtil.currentDate.toString(),


    )