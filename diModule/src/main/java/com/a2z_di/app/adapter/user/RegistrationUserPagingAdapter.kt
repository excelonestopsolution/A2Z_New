package com.a2z_di.app.adapter.user

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.a2z_di.app.R
import com.a2z_di.app.adapter.BasePagingRecyclerViewAdapter
import com.a2z_di.app.data.response.RegistrationRoleUser
import com.a2z_di.app.databinding.ListRegistrationUserBinding
import com.a2z_di.app.util.ents.setupTextColor

class RegistrationUserPagingAdapter : BasePagingRecyclerViewAdapter<RegistrationRoleUser,
        ListRegistrationUserBinding>(
    R.layout.list_registration_user, DiffUtilCallBack()
) {

    class DiffUtilCallBack : DiffUtil.ItemCallback<RegistrationRoleUser>() {
        override fun areItemsTheSame(oldItem: RegistrationRoleUser, newItem: RegistrationRoleUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RegistrationRoleUser, newItem: RegistrationRoleUser): Boolean {
            return false
        }

    }

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<ListRegistrationUserBinding>,
        position: Int
    ) {

        val item = this.getItem(position)
        val binding = holder.binding
        binding.item = item


        if(item?.id == 1){
            binding.rootLayout.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.black))
            binding.tvUserDetail.setupTextColor(R.color.white)
            binding.tvMobile.setupTextColor(R.color.white2)
        }
        else{
            binding.rootLayout.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.white2))
            binding.tvUserDetail.setupTextColor(R.color.color_primary)
            binding.tvMobile.setupTextColor(R.color.grey)
        }

        binding.rootLayout.setOnClickListener{
            onItemClick?.invoke(it,item!!,position)
        }

    }

}
