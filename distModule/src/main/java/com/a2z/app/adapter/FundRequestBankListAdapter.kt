package com.a2z.app.adapter

import com.a2z.di.R
import com.a2z.di.databinding.ListFundRequestBankBinding
import com.a2z.app.model.BankDetail

class FundRequestBankListAdapter() : BaseRecyclerViewAdapter<BankDetail, ListFundRequestBankBinding>
(R.layout.list_fund_request_bank) {

    override fun onBindViewHolder(holder: Companion.BaseViewHolder<ListFundRequestBankBinding>, position: Int) {
        val item = items[position]
        val binding = holder.binding

        binding.item = item

        binding.rootLayout.setOnClickListener {
            onItemClick?.invoke(it,item,position)
        }
    }
}