package com.di_md.a2z.activity.fund_request

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.di_md.a2z.R
import com.di_md.a2z.adapter.FundRequestBankListAdapter
import com.di_md.a2z.databinding.FragmentFundBankListBinding
import com.di_md.a2z.fragment.BaseFragment
import com.di_md.a2z.model.BankDetail
import com.di_md.a2z.util.apis.Resource
import com.di_md.a2z.util.dialogs.StatusDialog
import com.di_md.a2z.util.ents.hide
import com.di_md.a2z.util.ents.setup
import com.di_md.a2z.util.ents.setupToolbar
import com.di_md.a2z.util.ents.show
import com.di_md.a2z.util.enums.FundRequestType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FundBankListFragment () : BaseFragment<FragmentFundBankListBinding>(R.layout.fragment_fund_bank_list)  {


    interface OnBankClickListener{
        fun onBankClick(requestType: FundRequestType,bank : BankDetail )
    }

    private var listener : OnBankClickListener ? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as FundRequestActivity
    }



    private lateinit var toolbar : Toolbar

    private val viewModel : FundRequestViewModel by viewModels()
    private lateinit var requestType: FundRequestType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestType = arguments?.getParcelable("type")!!

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById(R.id.toolbar)
        setupToolbar(toolbar,"Select Bank")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.requestType = requestType
        viewModel.fetchBankList()
        subscribeObservers()


    }


    private fun subscribeObservers(){
        viewModel.bankListObs.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading ->{
                    binding.run {
                        progress.show()
                        recyclerView.hide()
                    }
                }
                is Resource.Success ->{
                    binding.run {
                        progress.hide()
                        recyclerView.show()
                    }
                    if(it.data.status == 1){
                        setupRecyclerViewList(it.data.banks)
                    }
                    else StatusDialog.failure(requireActivity(),it.data.message){
                        activity?.onBackPressed()
                    }

                }
                is Resource.Failure ->{
                    binding.run {
                        progress.hide()
                        recyclerView.show()
                    }
                    StatusDialog.failure(requireActivity(),it.exception.message.toString()){
                        activity?.onBackPressed()
                    }
                }
                else -> {}
            }
        })
    }

    private fun setupRecyclerViewList(banks: List<BankDetail>) {
        binding.recyclerView.setup().adapter = FundRequestBankListAdapter().apply {
            addItems(banks)
            context = activity

            onItemClick = {_,item,_->
                listener?.onBankClick(requestType,item)
            }
        }
        when(requestType){
            FundRequestType.CASH_DEPOSIT_MACHINE -> binding.cvNoteCdm.show()
            FundRequestType.CASH_COLLECT -> binding.cvNoteCashCollect.show()
            FundRequestType.BANK_TRANSFER -> binding.cvNoteBankTransfer.show()
            FundRequestType.CASH_DEPOSIT_COUNTER -> binding.cvNoteCashDeposit.show()
            else ->{}
        }
    }

    companion object{
        fun newInstance(type : FundRequestType) = FundBankListFragment().apply {
            arguments = bundleOf("type" to type)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}