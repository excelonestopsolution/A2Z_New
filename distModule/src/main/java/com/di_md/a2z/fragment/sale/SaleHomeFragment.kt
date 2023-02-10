package com.di_md.a2z.fragment.sale

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.di_md.a2z.AppPreference
import com.di_md.a2z.R
import com.di_md.a2z.activity.MainActivity
import com.di_md.a2z.activity.register.RegistrationActivity
import com.di_md.a2z.activity.register.user.RegisterUserListingActivity
import com.di_md.a2z.adapter.GridViewUserAdapter
import com.di_md.a2z.databinding.FragmentSaleHomeBinding
import com.di_md.a2z.fragment.BaseFragment
import com.di_md.a2z.listener.OnSaleItemClickListener
import com.di_md.a2z.util.AppConstants
import com.di_md.a2z.util.ents.launchIntent
import com.di_md.a2z.view.GridDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SaleHomeFragment() : BaseFragment<FragmentSaleHomeBinding>(R.layout.fragment_sale_home) {

    @Inject
    lateinit var appPreference: AppPreference

    companion object {
        fun newInstance() = SaleHomeFragment()

    }
    private var listner: OnSaleItemClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listner = activity as MainActivity

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        var mList = arrayListOf<String>()
        if(appPreference.rollId == 22) //RM
            mList = arrayListOf("ASM","API","FOS","MD","Distributor","Retailer")
        else if(appPreference.rollId == 23)//ASM
            mList = arrayListOf("FOS","MD","Distributor","Retailer")
        else if(appPreference.rollId == 24)//FOS
            mList = arrayListOf("MD","Distributor","Retailer")


        binding.recyclerView.addItemDecoration(
            GridDividerItemDecoration(
                spacing = 3
            )
        )
        binding.recyclerView.adapter = GridViewUserAdapter().apply {
            addItems(mList)
            onItemClick = {_,item,_->
                listner?.onSaleItemClick(item)
            }
        }

        binding.llCreateUser.setOnClickListener{
            activity?.launchIntent(RegistrationActivity::class.java, data = bundleOf(
                AppConstants.IS_SELF_REGISTRATION to false,
                AppConstants.SHOULD_MAP_ROLE to true,

            ))
        }

        binding.tvUsers.setOnClickListener{
            activity?.launchIntent(RegisterUserListingActivity::class.java)
        }
    }

}