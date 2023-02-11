package com.a2z_di.app.adapter

import com.a2z_di.app.R
import com.a2z_di.app.data.model.DmtTransactionDetail
import com.a2z_di.app.databinding.ListDmtResponseBinding


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