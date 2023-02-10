package com.di_md.a2z.activity.matm

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.di_md.a2z.R
import com.di_md.a2z.data.model.TransactionDetail
import com.di_md.a2z.data.model.matm.MatmTransactionResponse
import com.di_md.a2z.databinding.ActivityMatmResponseBinding
import com.di_md.a2z.util.APIs
import com.di_md.a2z.util.AppConstants
import com.di_md.a2z.util.PermissionHandler2
import com.di_md.a2z.util.VolleyClient
import com.di_md.a2z.util.dialogs.StatusDialog
import com.di_md.a2z.util.ents.*
import com.di_md.a2z.util.file.StorageHelper
import com.di_md.a2z.util.pdf.PdfHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MatmResponseActivity : AppCompatActivity() {


    @Inject
    lateinit var volleyClient: VolleyClient

    private lateinit var binding: ActivityMatmResponseBinding
    private var origin: String? = null
    private var shouldBack: Boolean? = null
    private var recordId: String? = null
    private var reportOrigin: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatmResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)



        shouldBack = intent.getBooleanExtra(AppConstants.ABLE_TO_BACK, false)
        origin = intent.getStringExtra(AppConstants.ORIGIN)
        reportOrigin = intent.getStringExtra(AppConstants.REPORT_ORIGIN) ?: ""

        if (origin == AppConstants.TRANSACTION_ORIGIN) {
            val response = intent.getParcelableExtra<MatmTransactionResponse>(AppConstants.DATA)
            handleTransactionData(response)
        } else {
            val response = intent.getParcelableExtra<TransactionDetail>(AppConstants.DATA_OBJECT)
            handleReportData(response)
        }

        binding.btnShare.setOnClickListener { onShareButtonClick() }
        binding.btnShareWhatsapp.setOnClickListener { onShareButtonClick(true) }
        binding.llDownloadReceipt.setOnClickListener { onDownloadReceiptButtonClick() }
        binding.btnClose.setOnClickListener { onBackPressed() }

    }

    private fun handleReportData(response: TransactionDetail?) {
        setupStatus(response?.status)
        recordId = response?.reportId
        binding.apply {
            tvStatus.text = response?.statusDesc
            tvMessage.text = response?.message
            tvTxnTime.text = response?.txnTime
            tvTransactionType.text = response?.txnType
            tvAmount.text = response?.amount
            tvServiceName.text = response?.serviceName
            tvMobileNumber.text = response?.senderNumber
            tvOrderId.text = response?.reportId
            tvBankRef.text = response?.bankRef
            tvCardNumber.text = response?.number
            tvCardType.text = response?.bankName
            tvAvailBalance.text = response?.availableBalance
            tvShopName.text = response?.outletName
            tvContactNumber.text = response?.outletNumber


            hideAmountInEnquiry(response?.txnType)

            if (response == null) return
            if (response.reportId.nullOrEmpty()) llOrderId.hide()
            if (response.bankRef.nullOrEmpty()) llBankRef.hide()
            if (response.number.nullOrEmpty()) llCardNumber.hide()
            if (response.senderNumber.nullOrEmpty()) llMobile.hide()
            if (response.cardType.nullOrEmpty()) llCardType.hide()
            llTxnMode.hide()
            llTxnId.hide()


        }
    }

    private fun handleTransactionData(response: MatmTransactionResponse?) {
        setupStatus(response?.status)


        recordId = response?.recordId
        binding.apply {
            tvStatus.text = response?.statusDesc
            tvMessage.text = response?.message
            tvTxnTime.text = response?.txnTime
            tvTransactionType.text = response?.transactionType
            tvAmount.text = response?.transactionAmount
            tvServiceName.text = response?.serviceName
            tvMobileNumber.text = response?.customerNumber
            tvTxnId.text = response?.txnId
            tvOrderId.text = response?.orderId
            tvBankRef.text = response?.bankRef
            tvCardNumber.text = response?.cardNumber
            tvCardType.text = response?.creditDebitCardType + " (" + response?.cardType + ")"
            tvAvailBalance.text = response?.availableAmount
            tvShopName.text = response?.shopName
            tvContactNumber.text = response?.retailerNumber
            tvTxnMode.text = response?.transactionMode

            hideAmountInEnquiry(response?.transactionType)

            if (response == null) return
            if (response.txnId.nullOrEmpty()) llTxnId.hide()
            if (response.orderId.nullOrEmpty()) llOrderId.hide()
            if (response.bankRef.nullOrEmpty()) llBankRef.hide()
            if (response.cardNumber.nullOrEmpty()) llCardNumber.hide()
            if (response.customerNumber.nullOrEmpty()) llMobile.hide()
            if (response.cardType.nullOrEmpty()) llCardType.hide()
            if (response.transactionMode.nullOrEmpty()) llTxnMode.hide()

        }
    }

    private fun hideAmountInEnquiry(type: String?) {
        if (type.toString().equals("matm(balance_enquiry)", ignoreCase = true)) {
            binding.tvAmount.hide()
        }

        if (!type.toString().equals("matm(cash_withdrawal)", ignoreCase = true) && !type.toString()
                .equals("matm(balance_enquiry)", ignoreCase = true)
        ) {
            binding.llAvailableBalance.hide();
        }
    }


    private fun setupStatus(status: Int?) {
        binding.let {
            when (status) {
                1 -> {
                    it.ivImage.setImageResource(R.drawable.icon_sucess2)
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.success))
                    it.tvStatus.setupTextColor(R.color.success)
                }
                2 -> {
                    it.ivImage.setImageResource(R.drawable.icon_failed)
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.red))
                    it.tvStatus.setupTextColor(R.color.red)
                }
                3 -> {
                    it.ivImage.setImageResource(R.drawable.pending)
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.yellow_dark))
                    it.tvStatus.setupTextColor(R.color.yellow_dark)
                    it.cvOperator.hide()
                    it.btnCheckStatus.show()
                }
                else -> {
                    it.ivImage.setImageResource(R.drawable.icon_sucess2)
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                    it.tvStatus.setupTextColor(R.color.colorPrimary)
                }
            }
        }

    }

    override fun onBackPressed() {
        if (shouldBack == null) goToMainActivity()
        if (shouldBack!! || origin == AppConstants.REPORT_ORIGIN) super.onBackPressed()
        else goToMainActivity()

    }

    fun checkStatusButtonCall(v:View){

        val dialog = StatusDialog.progress(this)

        volleyClient.getRequest(
            APIs.MATM_DIRECT_CHECK_STATUS,
            hashMapOf("recordId" to recordId.toString()),
            onSuccess = {
                dialog.dismiss()
                val status = it.getInt("status")
                val message = it.optString("message")
                when (status) {
                    1 -> StatusDialog.success(this, message)
                    2 -> StatusDialog.failure(this, message)
                    3 -> StatusDialog.pending(this, message)
                    else -> StatusDialog.alert(this, message)
                }
            },
            onFailure = {
                dialog.dismiss()
                this.showToast(it.message.toString())
            })
    }

    private fun onShareButtonClick(isWhatsAppOnly: Boolean = false) {
        PermissionHandler2.checkStoragePermission(this) { isGranted ->
            if (!isGranted) return@checkStoragePermission

            binding.viewSpace.hide()
            binding.llDownloadReceipt.hide()
            binding.scrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.black))

            val bitmap: Bitmap? = StorageHelper.getBitmapFromView(scrollView = binding.scrollView,
                onAfterConvert = {
                    binding.scrollView.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white2
                        )
                    )
                    binding.viewSpace.show()
                    binding.llDownloadReceipt.show()
                })

            val imageFileUri: Uri = StorageHelper.saveImageToCacheDirectory(
                context = this,
                bitmap = bitmap,
                fileName = "transaction_receipt.jpg"
            )
            StorageHelper.shareImage(imageFileUri, this, isWhatsAppOnly)

        }
    }


    private fun onDownloadReceiptButtonClick() {
        PermissionHandler2.checkStoragePermission(this, onPermissionGranted = {
            if (it) {
                PdfHelper.downloadPdfData(
                    this,
                    recordId.toString(),
                    isMatm = reportOrigin == AppConstants.MATM_REPORT
                            || origin == AppConstants.TRANSACTION_ORIGIN
                )
            }
        })
    }

}

