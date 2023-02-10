package com.a2z.app.adapter

import com.a2z.app.R
import com.a2z.app.model.DmtTransactionDetail
import com.a2z.app.databinding.ListDmtResponseBinding


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