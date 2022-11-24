package com.a2z.app.ui.util.resource

sealed class BannerType {
    object None : BannerType()
    data class Success(
        val title: String,
        val message: String,
    ) : BannerType()

    data class Failure(
        val title: String,
        val message: String
    ) : BannerType()

    data class Alert(
        val title: String,
        val message: String
    ) : BannerType()

}