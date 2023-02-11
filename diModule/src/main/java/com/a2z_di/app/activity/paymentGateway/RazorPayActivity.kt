package com.a2z_di.app.activity.paymentGateway

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.a2z_di.app.AppPreference
import com.a2z_di.app.R
import com.a2z_di.app.databinding.ActivityRazorPayBinding
import com.a2z_di.app.util.AppSecurity
import com.a2z_di.app.util.apis.Resource
import com.a2z_di.app.util.dialogs.StatusDialog
import com.a2z_di.app.util.ents.setupToolbar
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject

@AndroidEntryPoint
class RazorPayActivity : AppCompatActivity(), PaymentResultListener{

    private val viewModel : RazorPayViewModel by viewModels()
    var progressDialog : Dialog? = null
    lateinit var binding: ActivityRazorPayBinding

    lateinit var appPreference: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_razor_pay)

        appPreference = AppPreference.getInstance(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setupToolbar(toolbar, "Razorpay")
        getObserver()
    }

    fun getOrderID(v :View){
        val amount = binding.edAmount.text.toString().toLong()
        val mobile = binding.edMobileNumber.text.toString().toLong()
        viewModel.getOrderNumber(amount,mobile)
    }

    fun setDataOnRazorPay( ackNo :String){

        val amt = binding.edAmount.text.toString().trim()

        // rounding off the amount.
        val amount = Math.round(amt.toFloat() * 100).toInt()

        // on below line we are
        // initializing razorpay account
        val checkout = Checkout()

        // on the below line we have to see our id.
        checkout.setKeyID("rzp_live_YyEpr9gCxrbvHk")

        // set image
        checkout.setImage(R.mipmap.ic_launcher)

        // initialize json object
        val obj = JSONObject()

        try {
            // to put name
            obj.put("name", "A2ZSuvidhaa")
            obj.put("currency", "INR")
            obj.put("amount", amount)
            obj.put("receipt",ackNo)
            obj.put("theme.color", "#1A5C91")
            obj.put("prefill.contact", binding.edMobileNumber.text.toString())
            obj.put("prefill.email", AppSecurity.decrypt(appPreference.email))


            // open razorpay to checkout activity
            checkout.open(this@RazorPayActivity, obj)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun getObserver(){

        viewModel.orderInfo.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    progressDialog = StatusDialog.progress(this, "Loading...")
                }
                is Resource.Success -> {
                    progressDialog?.dismiss()

                    if (it.data.status == 1) {
                        Log.v("MyDATA",""+it.data.toString())
                        viewModel.ackNumber = it.data.data.ackno
                        setDataOnRazorPay(viewModel.ackNumber)
                    }
                    else{
                        StatusDialog.showErrorMessage(this, "" + it.data.message,
                            DialogInterface.OnClickListener { dialogInterface, i -> })
                    }
                }
                is Resource.Failure -> {
                    progressDialog?.dismiss()
                    StatusDialog.showErrorMessage(this, "" + it.exception.message,
                        DialogInterface.OnClickListener { dialogInterface, i -> })
                }
                else -> {}
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Log.d("RazorPay Success : ",""+p0)
       // Toast.makeText(this,"Payment Success",Toast.LENGTH_SHORT).show()
        StatusDialog.showErrorMessage(this, "Payment Success",
            DialogInterface.OnClickListener { dialogInterface, i -> })
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.d("RazorPay Error : ",""+p1)
        Toast.makeText(this,"Payment Error "+p1,Toast.LENGTH_SHORT).show()

    }

}