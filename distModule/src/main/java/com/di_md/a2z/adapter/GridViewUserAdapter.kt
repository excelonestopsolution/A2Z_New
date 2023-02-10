package com.di_md.a2z.adapter

import com.di_md.a2z.R
import com.di_md.a2z.databinding.ListGridUserBinding


class GridViewUserAdapter() : BaseRecyclerViewAdapter<String, ListGridUserBinding>(R.layout.list_grid_user) {
    override fun onBindViewHolder(holder: Companion.BaseViewHolder<ListGridUserBinding>, position: Int) {
        val binding = holder.binding
        val item = items[position]
        binding.tvTitle.text = item

        binding.cvContent.setOnClickListener{
            onItemClick?.invoke(it,item,position)
        }
    }
}