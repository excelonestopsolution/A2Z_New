package com.a2z_di.app.activity.fund_request

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.a2z_di.app.R
import com.a2z_di.app.model.BankDetail
import com.a2z_di.app.util.AppConstants
import com.a2z_di.app.util.ents.showToast
import com.a2z_di.app.util.enums.FundRequestType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FundRequestActivity : AppCompatActivity(),
        FundRequestTypeFragment.OnFundRequestTypeFragmentItemClickListener,
        FundBankListFragment.OnBankClickListener {

    private lateinit var origin: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fund_request2)


        origin = intent.getStringExtra(AppConstants.ORIGIN)!!



        if (origin == "distributor")
            addFragment(FundRequestFragment.newInstance())
        else
            addFragment(FundRequestTypeFragment.newInstance())


    }


    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
    }

    private fun addFragmentToBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit()
    }

    override fun onFundRequestTypeFragmentItemClick(type: FundRequestType) {
        when (type) {
            FundRequestType.UPI_PAYMENT -> {
                addFragmentToBackStack(UpiPaymentFragment.newInstance())
            }
            FundRequestType.PAYMENT_GATEWAY -> {
                showToast("Service is coming soon...")
                //launchIntent(PaymentGatewayActivity::class.java)
            }
            else -> addFragmentToBackStack(FundBankListFragment.newInstance(type))

        }
    }

    override fun onBankClick(requestType: FundRequestType, bank: BankDetail) {
        addFragmentToBackStack(FundRequestFragment.newInstance(requestType, bank))
    }


}