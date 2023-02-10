package com.a2z.app.adapter

import com.a2z.di.R
import com.a2z.di.databinding.ListGridUserBinding


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