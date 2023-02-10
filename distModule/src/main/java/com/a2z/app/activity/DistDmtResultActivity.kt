package com.a2z.app.activity

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
import com.a2z.app.BuildConfig
import com.a2z.app.R
import com.a2z.app.adapter.DmtTransactionAdapter
import com.a2z.app.model.DmtTransactionDetail
import com.a2z.app.model.TransactionDetail
import com.a2z.app.databinding.ActivityDmtResponseBinding
import com.a2z.app.fragment.report.ReportViewModel
import com.a2z.app.util.AppConstants
import com.a2z.app.util.PermissionHandler2
import com.a2z.app.util.apis.Resource
import com.a2z.app.util.dialogs.Dialogs
import com.a2z.app.util.dialogs.StatusDialog
import com.a2z.app.util.ents.*
import com.a2z.app.util.file.StorageHelper
import com.a2z.app.util.pdf.PdfHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DistDmtResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDmtResponseBinding
    private lateinit var origin: String
    private var status: Int? = null
    private var transactionId: String? = null
    private var serviceName: String? = null

    private val viewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDmtResponseBinding.inflate(layoutInflater)

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

    private fun setupListData(listData: List<DmtTransactionDetail>) {
        binding.recyclerView.setup().adapter = DmtTransactionAdapter().apply {
            this.context = this@DistDmtResultActivity
            this.addItems(listData)
        }
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
                    it.ivImage.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                    it.tvStatus.setupTextColor(R.color.colorPrimary)
                }
            }
        }

    }


    private fun setupDataFromTransaction() {
        val printResponse: TransactionDetail? = intent.getParcelableExtra(AppConstants.DATA_OBJECT)
        status = printResponse?.status
        serviceName = printResponse?.serviceName
        transactionId = printResponse?.reportId
        binding.let {
            it.tvStatus.text = printResponse?.statusDesc.orEmpty()
            it.tvMessage.text = printResponse?.message.orEmpty()
            it.tvTxnTime.text = printResponse?.txnTime.orEmpty()
            it.tvServiceName.text = printResponse?.serviceName.orEmpty()
            it.tvAmount.text = printResponse?.amount.orEmpty()
            it.tvBankName.text = printResponse?.bankName.orEmpty()
            it.tvAccountNumber.text = printResponse?.number.orEmpty()
            it.tvBeneName.text = printResponse?.name.orEmpty()
            it.tvTransactionType.text = printResponse?.paymentChannel.orEmpty()

            it.tvPaymentAmount.text = printResponse?.amount.orEmpty()
            it.tvWalletAmount.text = printResponse?.amount.orEmpty()

            if(serviceName.equals("dmt one", ignoreCase = true)){
                it.tvPaymentAmount.text= printResponse?.totalAmount.orEmpty()
                it.tvWalletAmount.text= printResponse?.totalAmount.orEmpty()
            }

            it.tvShopName.text = printResponse?.outletName.orEmpty()
            it.tvContactNumber.text = printResponse?.outletNumber.orEmpty()
            it.tvIfsc.text = printResponse?.ifsc.orEmpty()
        }
        setupStatus()
        printResponse?.dmtDetails?.let { setupListData(it) }

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
                    if (status != 2) binding.llDownloadReceipt.show()
                })

            val imageFileUri: Uri = StorageHelper.saveImageToCacheDirectory(
                context = this,
                bitmap = bitmap,
                fileName = "transaction_receipt.jpg"
            )
            StorageHelper.shareImage(imageFileUri, this, isWhatsAppOnly)

        }
    }

    private fun onDownloadReceipt() {
        PermissionHandler2.checkStoragePermission(this, onPermissionGranted = {
            if (it) {

                if (serviceName.equals("dmt one", ignoreCase = true)) {
                    PdfHelper.downloadPdfData(
                        this@DistDmtResultActivity,
                        transactionId.orEmpty(),
                        "0"
                    )
                } else Dialogs.dmtCommissionAmount(this).apply {
                    val edCommission = findViewById<EditText>(R.id.ed_commission)
                    val btnSkip = findViewById<Button>(R.id.btn_skip)
                    val btnSubmit = findViewById<Button>(R.id.btn_submit)

                    btnSkip.setOnClickListener {
                        dismiss()
                        PdfHelper.downloadPdfData(
                            this@DistDmtResultActivity,
                            transactionId.orEmpty(),
                            "0"
                        )
                    }
                    btnSubmit.setOnClickListener {
                        dismiss()
                        val commission =
                            if (edCommission.text.toString() == "") "0" else edCommission.text.toString()
                        PdfHelper.downloadPdfData(
                            this@DistDmtResultActivity,
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