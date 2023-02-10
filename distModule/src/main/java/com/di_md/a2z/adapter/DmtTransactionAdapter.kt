package com.di_md.a2z.adapter

import com.di_md.a2z.R
import com.di_md.a2z.data.model.DmtTransactionDetail
import com.di_md.a2z.databinding.ListDmtResponseBinding


class DmtTransactionAdapter() :
    BaseRecyclerViewAdapter<DmtTransactionDetail, ListDmtResponseBinding>(R.layout.list_dmt_response) {
    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<ListDmtResponseBinding>,
        position: Int
    ) {
        val binding = holder.binding
        val item = items[position]
        binding.item = item
    }
}