package com.a2z.app.activity.fund_request

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.a2z.app.AppPreference
import com.a2z.app.R
import com.a2z.app.databinding.FragmentUpiPaymentBinding
import com.a2z.app.fragment.BaseFragment
import com.a2z.app.listener.WebApiCallListener
import com.a2z.app.util.APIs
import com.a2z.app.util.AppSecurity
import com.a2z.app.util.WebApiCall
import com.a2z.app.util.dialogs.StatusDialog
import com.a2z.app.util.ents.goToMainActivity
import com.a2z.app.util.ents.setupToolbar
import com.a2z.app.util.ents.showToast
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class UpiPaymentFragment() : BaseFragment<FragmentUpiPaymentBinding>(R.layout.fragment_upi_payment) {

    @Inject
    lateinit var appPreference: AppPreference


    private lateinit var toolbar: Toolbar
    private lateinit var amount: String

    var  isQRCodeGenerate = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)
        setupToolbar(toolbar, "UPI Payment")

        binding.btnProceed.setOnClickListener {
            if (!validateInput()) return@setOnClickListener
            isQRCodeGenerate = false
            checkSetupService()
        }

        binding.btnQRCode.setOnClickListener{
            if (!validateInput()) return@setOnClickListener
            isQRCodeGenerate = true
            checkSetupService()
        }

        binding.edAmount.requestFocus()

    }

    private fun validateInput(): Boolean {

        var isValid = true

        amount = binding.edAmount.text.toString()
        if (amount.isEmpty()) amount = "0"

        if (amount.toDouble() == 0.0) {

            isValid = false
            binding.tilAmount.error = "Amount can't be zero!"
            binding.tilAmount.isErrorEnabled = true


        } else {
            binding.tilAmount.isErrorEnabled = false
        }
        return isValid
    }

    private fun checkSetupService() {

        progressDialog = StatusDialog.progress(requireActivity())

        val param = HashMap<String, String>()
        param["amount"] = amount

        WebApiCall.postRequest(requireActivity(), APIs.GENERATE_QRCODE_URL, param)
        WebApiCall.webApiCallback(object : WebApiCallListener {
            override fun onSuccessResponse(jsonObject: JSONObject) {

                progressDialog?.dismiss()

                val status = jsonObject.getInt("status")
                val message = jsonObject.optString("message")

                activity?.showToast(message)
                if (status == 1) {
                    val refNumber = jsonObject.getString("refId")
                    startPayment(refNumber)
                } else StatusDialog.failure(requireActivity(), message)

            }

            override fun onFailure(message: String) {
                progressDialog?.dismiss()
                StatusDialog.failure(requireActivity(), message)
            }

        })
    }

    private fun startPayment(refId: String) {
        try{
            val upiStr = "upi://pay?pa=excelone@icici&pn=Excel Stop&tr=$refId&am=$amount" +
                    "&cu=INR&mc=5411&tn=${AppSecurity.decrypt(appPreference.mobile)}${appPreference.shopName}"


            if (isQRCodeGenerate){
                showQRCode(upiStr)
            }else{
                val intent = Intent()
                //  intent.addCategory(Intent.CATEGORY_HOME);
                //intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.data = Uri.parse(upiStr)
                val chooser = Intent.createChooser(intent, "Pay with...")
                startActivityForResult(chooser, 1, null)
            }

        }catch (e : Exception){
            activity?.showToast("Failed to make payment! please try again")
        }
    }


    private fun showQRCode(strUrl: String) {

        try {
            val bitmap = textToImageEncode(strUrl)

            val alertDialog = AlertDialog.Builder(requireActivity())
            val customLayout: View = layoutInflater.inflate(R.layout.dialog_qr_code, null)
            alertDialog.setView(customLayout)

            val alert = alertDialog.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()

            val qrCode = customLayout.findViewById<ImageView>(R.id.iv_qr_code)
            qrCode.setImageBitmap(bitmap)
            qrCode.invalidate()
            val cancelButton = customLayout.findViewById<Button>(R.id.btn_cancel)
            cancelButton.setOnClickListener(){
                alert.dismiss()
            }

        } catch (e: WriterException) {
            e.printStackTrace()
        }

    }

    private fun textToImageEncode(Value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                Value,
                BarcodeFormat.QR_CODE,
                500, 500, null
            )
        } catch (exception: IllegalArgumentException) {
            return null
        }
        val bitMatrixWidth = bitMatrix.getWidth()
        val bitMatrixHeight = bitMatrix.getHeight()
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] = if (bitMatrix.get(x, y))
                    Color.parseColor("#000000")
                else Color.parseColor("#ffffff")
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1) activity?.goToMainActivity()

    }

    companion object {
        fun newInstance() = UpiPaymentFragment()
    }



}