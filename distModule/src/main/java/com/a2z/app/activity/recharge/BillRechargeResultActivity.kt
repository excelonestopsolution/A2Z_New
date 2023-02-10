package com.a2z.app.activity.recharge

import android.app.Dialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.a2z.di.BuildConfig
import com.a2z.di.R
import com.a2z.app.model.TransactionDetail
import com.a2z.di.databinding.ActivityRechargeAndBillResponseBinding
import com.a2z.app.fragment.report.ReportViewModel
import com.a2z.app.util.AppConstants
import com.a2z.app.util.PermissionHandler2
import com.a2z.app.util.apis.Resource
import com.a2z.app.util.dialogs.StatusDialog
import com.a2z.app.util.ents.*
import com.a2z.app.util.file.StorageHelper
import com.a2z.app.util.pdf.PdfHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillRechargeResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRechargeAndBillResponseBinding
    private lateinit var origin: String

    private lateinit var transactionId:String

    private val viewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRechargeAndBillResponseBinding.inflate(layoutInflater)

        setContentView(binding.root)
        origin = intent.getStringExtra(AppConstants.ORIGIN).orEmpty()

        if (origin == AppConstants.TRANSACTION_ORIGIN)
            setupDataFromTransaction()
        else setupDataFromReport()

    }

    override fun onBackPressed() {
        if (origin == AppConstants.TRANSACTION_ORIGIN) {
            goToMainActivity()
        } else
            super.onBackPressed()
    }

    private fun setupDataFromReport() {

        val transactionDetail: TransactionDetail? = intent.getParcelableExtra(AppConstants.DATA_OBJECT)
        if (transactionDetail != null) {
            transactionId = transactionDetail.reportId.toString()
        }
        transactionDetail?.let {

           if(it.reportId.orEmpty().isEmpty()) binding.llPayId.hide()
            if (it.name == null) binding.llBillerName.hide()
            if (it.bankRef.orEmpty().isEmpty()) binding.llOperatorRef.hide()

            binding.tvOrderId.text = it.reportId
            binding.tvTxnTime.text = it.txnTime
            binding.tvServiceName.text = it.serviceName
            binding.tvServiceName2.text = it.serviceName
            binding.tvOperatorRef.text = it.bankRef
            binding.tvBillerName.text = it.senderName
            binding.tvProviderName.text = it.provider
            binding.tvProviderName2.text = "Product for ${it.provider} Order"
            binding.tvAmount.text = it.amount
            binding.tvPaymentAmount.text = it.amount
            binding.tvWalletAmount.text = it.amount
            binding.tvMessage.text = it.message
            binding.tvNumber.text = it.number
            binding.tvNumberTitle.text = "Number"
            //todo  binding.cvOperator.setImageDrawable()
            binding.tvStatus.text = it.statusDesc

            setupStatus(it.status!!)
            downloadAndShare(it.reportId.toString(), it.status!!)

        }
    }

    private fun setupDataFromTransaction() {
        val payId = intent.getStringExtra("payId")
        val txnTime = intent.getStringExtra("txnTime")
        val operatorRef = intent.getStringExtra("operatorRef")
        val statusDescription = intent.getStringExtra("statusDescription")
        val message = intent.getStringExtra("message")
        val billerName = intent.getStringExtra("billerName")
        val providerName = intent.getStringExtra("providerName")
        val amount = intent.getStringExtra("amount")
        val recordId = intent.getStringExtra("recordId")
        val operatorIcon = intent.getStringExtra("operatorIcon")
        val serviceName = intent.getStringExtra("serviceName")
        val number = intent.getStringExtra("number")
        val numberTitle = intent.getStringExtra("numberTitle")
        val statusId = intent.getIntExtra("statusId", 0)
        val transactionType = intent.getStringExtra("transactionType")



        binding.let {
            it.tvOrderId.text = payId
            it.tvTxnTime.text = txnTime
            it.tvServiceName.text = serviceName
            it.tvServiceName2.text = serviceName
            it.tvOperatorRef.text = operatorRef
            it.tvBillerName.text = billerName
            it.tvProviderName.text = providerName
            it.tvProviderName2.text = "Product for $providerName Order"
            it.tvAmount.text = amount
            it.tvPaymentAmount.text = amount
            it.tvWalletAmount.text = amount
            it.tvMessage.text = message
            it.tvNumber.text = number
            it.tvNumberTitle.text = numberTitle
            it.cvOperator.setupPicassoImage(operatorIcon!!)
            it.tvStatus.text = statusDescription

            if (operatorRef.orEmpty().isEmpty()) binding.llOperatorRef.hide()
            if (billerName == AppConstants.EMPTY) binding.llBillerName.hide()
            if (payId == AppConstants.EMPTY) binding.llPayId.hide()

            setupStatus(statusId)
            downloadAndShare(recordId, statusId)

        }
    }

    private fun downloadAndShare(recordId: String?, statusId: Int) {
        binding.llDownloadReceipt.setOnClickListener {
            PermissionHandler2.checkStoragePermission(this) {
                PdfHelper.downloadPdfData(this, recordId!!)
            }
        }


        val scrollView = binding.scrollView
        val llDownloadReceipt = binding.llDownloadReceipt
        val viewSpace = binding.viewSpace

        binding.btnShareWhatsapp.setOnClickListener {
            onShareWhatsappButtonClick(viewSpace, llDownloadReceipt, scrollView, statusId)
        }

        binding.btnShare.setOnClickListener {
            onButtonShareClick(viewSpace, llDownloadReceipt, scrollView, statusId)
        }

        binding.btnClose.setOnClickListener { finish() }
    }

    private fun setupStatus(statusId: Int) {
        binding.let {
            when (statusId) {
                1, 24 -> {
                    it.ivImage.setImageResource(R.drawable.icon_sucess2)
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.success))
                    it.tvStatus.setupTextColor(R.color.success)
                    it.ivPaid.show()
                }
                2 -> {
                    it.ivImage.setImageResource(R.drawable.icon_failed)
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.red))
                    it.tvStatus.setupTextColor(R.color.red)
                    if (BuildConfig.DEBUG)
                        it.llDownloadReceipt.show()
                    else it.llDownloadReceipt.hide()
                    it.llServiceInfo.hide()
                }
                3 -> {
                    it.ivImage.setImageResource(R.drawable.pending)
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.yellow_dark))
                    it.tvStatus.setupTextColor(R.color.yellow_dark)
                    it.cvOperator.hide()
                    it.btnCheckStatus.show()
                }
                else -> {
                    it.ivImage.setImageResource(R.drawable.pending)
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                    it.tvStatus.setupTextColor(R.color.colorPrimary)
                }
            }
        }
    }

    private fun onButtonShareClick(viewSpace: View, llDownloadReceipt: LinearLayout, scrollView: ScrollView, statusId: Int) {
        PermissionHandler2.checkStoragePermission(this) { isGranted ->
            if (!isGranted) return@checkStoragePermission

            viewSpace.hide()
            llDownloadReceipt.hide()
            scrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.black))

            val bitmap: Bitmap? = StorageHelper.getBitmapFromView(scrollView = scrollView,
                    onAfterConvert = {
                        scrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.white2))
                        viewSpace.show()
                        if (statusId != 2) llDownloadReceipt.show()
                    })

            val imageFileUri: Uri = StorageHelper.saveImageToCacheDirectory(
                    context = this,
                    bitmap = bitmap,
                    fileName = "transaction_receipt.jpg",

                    )
            StorageHelper.shareImage(imageFileUri, this, false)

        }
    }

    private fun onShareWhatsappButtonClick(viewSpace: View, llDownloadReceipt: LinearLayout, scrollView: ScrollView, statusId: Int) {
        PermissionHandler2.checkStoragePermission(this) { isGranted ->
            if (!isGranted) return@checkStoragePermission

            viewSpace.hide()
            llDownloadReceipt.hide()
            scrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.black))

            val bitmap: Bitmap? = StorageHelper.getBitmapFromView(scrollView = scrollView,
                    onAfterConvert = {
                        scrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.white2))
                        viewSpace.show()
                        if (statusId != 2) llDownloadReceipt.show()
                    })

            val imageFileUri: Uri = StorageHelper.saveImageToCacheDirectory(
                    context = this,
                    bitmap = bitmap,
                    fileName = "transaction_receipt.jpg"

            )
            StorageHelper.shareImage(imageFileUri, this, true)
        }
    }

    fun checkStatusButtonCall(v:View){
        getObserver()
        viewModel.checkStatus(transactionId)
    }
    fun getObserver(){
        var progressDialog : Dialog? = null
        viewModel.checkStatusObs.observe(this){
            when (it) {
                is Resource.Loading -> {
                    progressDialog = StatusDialog.progress(this)
                }
                is Resource.Success -> {
                    progressDialog?.dismiss()
                    when (it.data.status) {
                        1 -> StatusDialog.success(this, it.data.message)
                        3 ->
                            StatusDialog.pending(this, it.data.message)
                        else -> StatusDialog.failure(this, it.data.message)
                    }
                }
                is Resource.Failure -> {
                    progressDialog?.dismiss()
                    this.handleNetworkFailure(it.exception)
                }
                else -> {}
            }
        }
    }
}