package com.a2z.app.ui.util.pdf

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.a2z.app.data.model.dmt.TransactionDetailResponse
import com.a2z.app.ui.screen.result.TxnResultPrintReceiptType
import com.a2z.app.util.extension.showToast
import io.github.lucasfsc.html2pdf.Html2Pdf
import java.io.File


object ReceiptDownloadUtil {

    fun download(
        context: Context,
        data: TransactionDetailResponse,
        commission: String?,
        txnResultPrintReceiptType: TxnResultPrintReceiptType
    ) {

        data.data?.let { transactionDetail ->

            val pdfString = when (txnResultPrintReceiptType) {
                TxnResultPrintReceiptType.AEPS -> aepsHtmlDataAsString(transactionDetail)
                TxnResultPrintReceiptType.MATM -> matmHtmlDataAsString(transactionDetail)
                TxnResultPrintReceiptType.OTHER -> when (transactionDetail.slipType) {
                    "UPI_PAYMENT" -> upiHtmlDataAsString(transactionDetail, commission)
                    "TRANSACTION" -> dmtHtmlDataAsString(transactionDetail, commission)
                    "BILLPAYMENT", "FASTTAG" -> bbpsHtmlDataAsString(transactionDetail)
                    "RECHARGE" -> rechargeHtmlDataAsString(transactionDetail)
                    "AEPS" -> aepsHtmlDataAsString(transactionDetail)
                    "MATM" -> matmHtmlDataAsString(transactionDetail)
                    else -> null
                }
            }

            pdfString?.let {
                generatePdfUriFromHtmlString(context, pdfString) {
                    viewPdf(it, context)
                }
            }
        }

    }


    private fun generatePdfUriFromHtmlString(
        context: Context,
        strPdfHtml: String,
        callback: (Uri?) -> Unit
    ) {

        context.cacheDir.deleteRecursively()
        val cachePath = File(context.externalCacheDir, "images/")
        cachePath.mkdirs()
        val file = File(cachePath, "${System.currentTimeMillis()}_a2z_receipt.pdf")

        val converter = Html2Pdf.Companion.Builder()
            .context(context)
            .html(strPdfHtml)
            .file(file)
            .build()

        //can be called with a callback to warn the user, share file...
        converter.convertToPdf(object : Html2Pdf.OnCompleteConversion {
            override fun onFailed() {
                context.showToast("Unable to download file")
                callback(null)
            }

            override fun onSuccess() {
                val photoURI: Uri = FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName.toString() + ".provider", file
                )
                callback(photoURI)
            }
        })
    }


    fun viewPdf(uri: Uri?, context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/pdf")
            context.startActivity(intent)
        } catch (e: Exception) {
            context.showToast("Application not found! ${e.message}")
        }
    }
}

