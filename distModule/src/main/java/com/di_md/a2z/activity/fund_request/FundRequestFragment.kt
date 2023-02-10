package com.di_md.a2z.activity.fund_request

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.di_md.a2z.ImageProcess.ImageCompressor
import com.di_md.a2z.R
import com.di_md.a2z.databinding.FragmentFundRequestBinding
import com.di_md.a2z.fragment.BaseFragment
import com.di_md.a2z.model.BankDetail
import com.di_md.a2z.util.*
import com.di_md.a2z.util.BitmapUtil.toFile
import com.di_md.a2z.util.apis.Resource
import com.di_md.a2z.util.dialogs.Dialogs
import com.di_md.a2z.util.dialogs.StatusDialog
import com.di_md.a2z.util.ents.*
import com.di_md.a2z.util.enums.FundRequestType
import com.di_md.a2z.util.file.AppFileUtil.toBitmap
import com.di_md.a2z.util.file.FragmentImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@AndroidEntryPoint
class FundRequestFragment() :
    BaseFragment<FragmentFundRequestBinding>(R.layout.fragment_fund_request) {

    private val viewModel: FundRequestViewModel by viewModels()

    private var requestType: FundRequestType? = null
    private var bank: BankDetail? = null

    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestType = requireArguments().getParcelable("type")
        bank = requireArguments().getParcelable("bank")

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.requestType = requestType
        viewModel.bank = bank

        toolbar = view.findViewById(R.id.toolbar)
        setupToolbar(toolbar, "Submit Request")

        viewModel.requestType?.let {
            viewModel.requestTo = "2"
            toolbar.title = "Fund Request"
            setupBankDetail()
            setupRefHint()
        }

        setupPaymentType()

        setupPaymentDate()

        binding.edSlip.setOnClickListener {
            FragmentImagePicker.pickMultiple(this)
        }
        binding.btnProceed.setOnClickListener {

            if (!validateFundRequestToCompanyInput()) return@setOnClickListener


            Dialogs.commonConfirmDialog(
                requireContext(),
                "Fund Request for Rs. ${viewModel.amount}"
            ).apply {
                findViewById<Button>(R.id.btn_confirm).setOnClickListener {
                    dismiss()
                    viewModel.submitRequest()
                }
            }

        }

        subscribeObserver()

    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!


            var filePath = FileUtils.getPath(context, uri)
            var file = File(filePath)

            binding.edSlip.setText(file.name)
            viewModel.fileSlip = file

            activity?.showToast("file size : " + file.sizeInKb)


        } else if (resultCode == ImagePicker.RESULT_ERROR) {

            Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
        *//* PickerHelperFragment.onActivityResult(this,requestCode, resultCode, data){
             binding.edSlip.setText(it.name)
             viewModel.fileSlip = it
         }*//*
    }
*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val file: File =
                FragmentImagePicker.onActivityResult(this, requestCode, resultCode, data) ?: return

            lifecycleScope.launch {
                val dialog = StatusDialog.progress(requireContext(), "Optimizing Image...")

//                val optimizeFile = withContext(Dispatchers.IO) {
////                    file.processForRightAngleImage()
////                        .toBitmap()
////                        .rotatePortrait()
////                        .reduceSize(10)
////                        .toFile(context)
//                    file.toBitmap().toFile(context)
//                }
//
//               // val optimizeFile: File? = AppFileUtil.saveBitmapToFile(file)
//                optimizeFile?.let {
//                    ImageCompressor.compressCurrentBitmapFile(file)
//                    AppUtil.logger("File Testing : ${it.sizeInKb}")
//                    setupSelectedImages(it)
//                } ?: kotlin.run { StatusDialog.failure(requireActivity(), "File not found!") }
//                dialog.dismiss()

//                ImageCompressor.compressCurrentBitmapFile(file)
//                file.let {
//                    AppUtil.logger("File Testing : ${it.sizeInKb}")
//                    setupSelectedImages(it)
//                } ?: kotlin.run { StatusDialog.failure(requireActivity(), "File not found!") }

                file.let {
                    val optimizeFile = withContext(Dispatchers.IO){
                         it.toBitmap().toFile(context)
                    }

                    optimizeFile?.let {
                        ImageCompressor.compressCurrentBitmapFile(it)
                        AppUtil.logger("file testing : file size :  ${it.sizeInKb}")
                        AppUtil.logger("file testing : file name : ${it.name}")
                        AppUtil.logger("file testing : file mime type : ${FileUtils.getMimeType(it)}")
                        setupSelectedImages(it)
                    }


//                    if (file2 != null) {
//
//                    }
                    dialog.dismiss()
                    //Dialogs.docImageView(requireActivity(), it.toBitmap().addWatermark(DateUtil.currentDateInMinuteHourSecond(),address1,address2,address3))
                }
            }

    }
    private fun setupSelectedImages(file: File) {


        binding.edSlip.setText(file.name)
        viewModel.fileSlip = file

        activity?.showToast("file size : " + file.sizeInKb)

    }


    private fun subscribeObserver() {
        viewModel.requestResultObs.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    progressDialog = StatusDialog.progress(requireActivity(), "Requesting...")
                }
                is Resource.Success -> {
                    progressDialog?.dismiss()

                    if (it.data.status == 1) {
                        StatusDialog.success(requireActivity(), it.data.message) {
                            viewModel.requestType?.let {
                                activity?.launchIntent(
                                    FundRequestActivity::class.java,
                                    bundleOf(AppConstants.ORIGIN to "topup")
                                )
                                activity?.finish()
                            } ?: run {
                                activity?.goToMainActivity()
                            }
                        }
                    } else StatusDialog.failure(requireActivity(), it.data.message)
                }

                is Resource.Failure -> {
                    progressDialog?.dismiss()
                    StatusDialog.alert(
                        requireActivity(),
                        "Connection terminate due to slow network! please try again"
                    )

                }
                else -> {}
            }
        })
    }

    private fun setupRefHint() {
        binding.tilRefNumber.show()
        val strRefHint = when (viewModel.requestType!!) {
            FundRequestType.BANK_TRANSFER -> viewModel.bank!!.bank_transfer_place_holder
            FundRequestType.CASH_DEPOSIT_COUNTER -> viewModel.bank!!.cash_deposit_place_holder
            FundRequestType.CASH_DEPOSIT_MACHINE -> viewModel.bank!!.cash_cdm_place_holder
            else -> "Ref Number"
        }
        if (strRefHint.isNotEmpty())
            binding.tilRefNumber.hint = strRefHint
    }

    private fun setupBankDetail() {

        bank?.let {
            binding.cvBankInfo.show()
            binding.tvAccounHolderName.text = it.messageOne
            binding.tvBankName.text = "Bank : " + it.bankName
            binding.tvAccountNumber.text = "A/C : " + it.account_number
            binding.tvIfsc.text = "IFSC : " + it.ifsc
            binding.tvCharge.text = it.charges
            if (it.charges.isNotEmpty())
                binding.tvCharge.show()
        }
    }

    private fun setupPaymentDate() {

        val currentDate = AppUitls.currentDate()
        viewModel.paymentDate = currentDate

        binding.edPaymentDate.setText(currentDate)
        binding.edPaymentDate.setOnClickListener {
            DatePicker.datePicker(requireActivity())
            DatePicker.setupOnDatePicker {
                binding.edPaymentDate.setText(it)
                viewModel.paymentDate = currentDate
            }
        }

    }

    private fun setupPaymentType() {
        val paymentMode: String
        when (requestType) {
            FundRequestType.BANK_TRANSFER -> {
                paymentMode = "Bank Transfer"
                viewModel.paymentMode = "OnLine"
            }
            FundRequestType.CASH_DEPOSIT_COUNTER -> {
                paymentMode = "Cash Deposit (Counter)"
                viewModel.paymentMode = "Cash@Counter"
            }
            FundRequestType.CASH_DEPOSIT_MACHINE -> {
                paymentMode = "Cash Deposit (Machine)"
                viewModel.paymentMode = "Cash@CDM"
            }
            FundRequestType.CASH_COLLECT -> {
                paymentMode = "Cash Collect"
                viewModel.paymentMode = "Cash@Collect"
            }
            null -> {
                paymentMode = "CASH"
                viewModel.paymentMode = "Cash"
            }
            else -> {
                paymentMode = ""
                viewModel.paymentMode = ""
            }
        }
        binding.edPaymentType.setText(paymentMode)
    }


    companion object {

        fun newInstance(type: FundRequestType? = null, bank: BankDetail? = null) =
            FundRequestFragment().apply {
                arguments = bundleOf(
                    "type" to type,
                    "bank" to bank
                )
            }
    }


    private fun validateFundRequestToCompanyInput(): Boolean {

        var isValid = true

        viewModel.amount = binding.edAmount.text.toString()
        viewModel.refNumber = binding.edRefNumber.text.toString()
        if (viewModel.amount.isEmpty()) viewModel.amount = "0"

        if (viewModel.amount.toDouble() < 1) {
            isValid = false
            binding.tilAmount.error = "Enter min amount 1 Rs."
            binding.tilAmount.isErrorEnabled = true

        } else binding.tilAmount.isErrorEnabled = false

        if (viewModel.refNumber.isEmpty() && requestType != null) {
            isValid = false
            binding.tilRefNumber.error = "Ref number can't be empty!"
            binding.tilRefNumber.isErrorEnabled = true

        } else binding.tilRefNumber.isErrorEnabled = false

        if (viewModel.fileSlip == null && requestType != null) {
            isValid = false
            binding.tilUploadSlip.error = "Upload slip file!"
            binding.tilUploadSlip.isErrorEnabled = true

        } else binding.tilUploadSlip.isErrorEnabled = false


        return isValid

    }

}