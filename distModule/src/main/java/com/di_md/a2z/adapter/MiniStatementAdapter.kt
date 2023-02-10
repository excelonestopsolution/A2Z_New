package com.di_md.a2z.adapter

import com.di_md.a2z.R
import com.di_md.a2z.data.model.MiniStatement
import com.di_md.a2z.databinding.ListMiniStatementBinding
import com.di_md.a2z.util.ents.setupTextColor


class MiniStatementAdapter() : BaseRecyclerViewAdapter<MiniStatement, ListMiniStatementBinding>(R.layout.list_mini_statement) {
    override fun onBindViewHolder(holder: Companion.BaseViewHolder<ListMiniStatementBinding>, position: Int) {
        val binding = holder.binding
        val item = items[position]
        binding.miniStatement = item

        if (item.txnType == "Dr")
            binding.tvAmount.setupTextColor(R.color.red)
        else binding.tvAmount.setupTextColor(R.color.success)
    }
}