package com.a2z_di.app.adapter

import com.a2z_di.app.R
import com.a2z_di.app.data.model.MiniStatement
import com.a2z_di.app.databinding.ListMiniStatementBinding
import com.a2z_di.app.util.ents.setupTextColor


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