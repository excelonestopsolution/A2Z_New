package com.di_md.a2z.adapter

import com.di_md.a2z.R
import com.di_md.a2z.databinding.ListFundRequestBankBinding
import com.di_md.a2z.model.BankDetail

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