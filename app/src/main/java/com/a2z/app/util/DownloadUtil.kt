package com.a2z.app.util

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import java.text.SimpleDateFormat
import java.util.*

object DownloadUtil {

    @SuppressLint("SimpleDateFormat")
    operator fun invoke(
        downloadManager: DownloadManager,
        downloadUrl : String,
        fileName: String,
        fileExtension : String,
        description: String="A2Z file download"
    ) {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val mFileName = "A2Z_File_"+fileName +"_"+timeStamp + "."+fileExtension
        val request = DownloadManager.Request(Uri.parse(downloadUrl))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(mFileName)
            .setDescription(description)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        downloadManager.enqueue(request)
    }
}