package com.a2z_di.app.activity

import android.app.Dialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.a2z_di.app.BuildConfig
import com.a2z_di.app.R
import com.a2z_di.app.data.model.TransactionDetail
import com.a2z_di.app.databinding.ActivityUpiResponseBinding
import com.a2z_di.app.fragment.report.ReportViewModel
import com.a2z_di.app.util.AppConstants
import com.a2z_di.app.util.PermissionHandler2
import com.a2z_di.app.util.apis.Resource
import com.a2z_di.app.util.dialogs.Dialogs
import com.a2z_di.app.util.dialogs.StatusDialog
import com.a2z_di.app.util.ents.*
import com.a2z_di.app.util.file.StorageHelper
import com.a2z_di.app.util.pdf.PdfHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DistUpiResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpiResponseBinding
    private lateinit var origin: String
    private var status: Int? = null
    private var transactionId: String? = null

    private val viewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpiResponseBinding.inflate(layoutInflater)

        setContentView(binding.root)
        origin = intent.getStringExtra(AppConstants.ORIGIN)!!


        setupDataFromTransaction()

        binding.let {
            it.btnClose.setOnClickListener { onBackPressed() }
            it.btnShareWhatsapp.setOnClickListener { onShareButtonClick(true) }
            it.btnShare.setOnClickListener { onShareButtonClick(false) }
            it.llDownloadReceipt.setOnClickListener { onDownloadReceipt() }
        }
    }

    override fun onBackPressed() {
        if (origin == AppConstants.REPORT_ORIGIN) super.onBackPressed()
        else goToMainActivity()
    }


    private fun setupStatus() {
        binding.let {
            when (status) {
                1, 24 -> {
                    it.ivImage.setImageResource(R.drawable.icon_sucess2)
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.success))
                    it.tvStatus.setupTextColor(R.color.success)
                }
                2 -> {
                    it.ivImage.setImageResource(R.drawable.icon_failed)
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.red))
                    it.tvStatus.setupTextColor(R.color.red)
                    if (BuildConfig.DEBUG)
                        it.llDownloadReceipt.show()
                    else it.llDownloadReceipt.hide()
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
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.color_primary))
                    it.tvStatus.setupTextColor(R.color.color_primary)
                }
            }
        }

    }


    private fun setupDataFromTransaction() {
        val printResponse: TransactionDetail? = intent.getParcelableExtra(AppConstants.DATA_OBJECT)
        status = printResponse?.status
        transactionId = printResponse?.reportId
        binding.let {
            it.tvStatus.text = printResponse?.statusDesc.orEmpty()
            it.tvMessage.text = printResponse?.message.orEmpty()
            it.tvTxnTime.text = printResponse?.txnTime.orEmpty()
            it.tvServiceName.text = printResponse?.serviceName.orEmpty()
            it.tvAmount.text = printResponse?.amount.orEmpty()
            it.tvSenderName.text = printResponse?.senderName.orEmpty()
            it.tvAccountNumber.text = printResponse?.number.orEmpty()
            it.tvBeneName.text = printResponse?.name.orEmpty()
            it.tvSenderNumber.text = printResponse?.senderNumber.orEmpty()
            it.tvPaymentAmount.text = printResponse?.amount.orEmpty()
            it.tvWalletAmount.text = printResponse?.amount.orEmpty()
            it.tvShopName.text = printResponse?.outletName.orEmpty()
            it.tvContactNumber.text = printResponse?.outletNumber.orEmpty()
            it.tvTxnId.text = printResponse?.reportId.orEmpty()
            it.tvBankRef.text = printResponse?.bankRef.orEmpty()

            if(printResponse?.reportId.orEmpty().isEmpty()) binding.llTxnId.hide()
            if(printResponse?.bankRef.orEmpty().isEmpty()) binding.llBankRef.hide()
        }
        setupStatus()

    }


    private fun onShareButtonClick(isWhatsAppOnly: Boolean = false) {
        PermissionHandler2.checkStoragePermission(this) { isGranted ->
            if (!isGranted) return@checkStoragePermission

            binding.viewSpace.hide()
            binding.llDownloadReceipt.hide()
            binding.scrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.black))

            val bitmap: Bitmap? = StorageHelper.getBitmapFromView(scrollView = binding.scrollView,
                    onAfterConvert = {
                        binding.scrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.white2))
                        binding.viewSpace.show()
                        if (status != 2) binding.llDownloadReceipt.show()
                    })

            val imageFileUri: Uri = StorageHelper.saveImageToCacheDirectory(
                    context = this,
                    bitmap = bitmap,
                    fileName = "transaction_receipt.jpg")
            StorageHelper.shareImage(imageFileUri, this, isWhatsAppOnly)

        }
    }

    private fun onDownloadReceipt() {
        PermissionHandler2.checkStoragePermission(this, onPermissionGranted = {
            if (it) {

                Dialogs.dmtCommissionAmount(this).apply {
                    val edCommission = findViewById<EditText>(R.id.ed_commission)
                    val btnSkip = findViewById<Button>(R.id.btn_skip)
                    val btnSubmit = findViewById<Button>(R.id.btn_submit)

                    btnSkip.setOnClickListener {
                        dismiss()
                        PdfHelper.downloadPdfData(
                                this@DistUpiResultActivity,
                                transactionId.orEmpty(),
                                "0"
                        )
                    }
                    btnSubmit.setOnClickListener {
                        dismiss()
                        val commission = if (edCommission.text.toString() == "") "0" else edCommission.text.toString()
                        PdfHelper.downloadPdfData(
                                this@DistUpiResultActivity,
                                transactionId.orEmpty(),
                                commission
                        )
                    }
                }

            }
        })
    }

    fun checkStatusButtonCall(v: View){
        getObserver()
        viewModel.checkStatus(transactionId.toString())
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