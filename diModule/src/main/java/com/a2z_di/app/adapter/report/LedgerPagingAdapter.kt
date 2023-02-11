package com.a2z_di.app.adapter.report

import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.a2z_di.app.R
import com.a2z_di.app.adapter.BasePagingRecyclerViewAdapter
import com.a2z_di.app.data.model.report.LedgerReport
import com.a2z_di.app.databinding.ListLedgerReport2Binding
import com.a2z_di.app.util.ents.hide
import com.a2z_di.app.util.ents.setupTextColor
import com.a2z_di.app.util.ents.show

class LedgerPagingAdapter : BasePagingRecyclerViewAdapter<LedgerReport, ListLedgerReport2Binding>(
    R.layout.list_ledger_report_2, DiffUtilCallBack()
) {

    class DiffUtilCallBack : DiffUtil.ItemCallback<LedgerReport>() {
        override fun areItemsTheSame(oldItem: LedgerReport, newItem: LedgerReport): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LedgerReport, newItem: LedgerReport): Boolean {
            return false
        }

    }

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<ListLedgerReport2Binding>,
        position: Int
    ) {

        val item = this.getItem(position)
        val binding = holder.binding
        binding.item = item
        binding.rootLayout.setOnClickListener {
            setupExpandedLayout(item!!, holder.binding)
        }

        when {
            item?.beneName.orEmpty().isNotEmpty() -> {
                binding.tvName.text = item?.beneName
                binding.llName.show()
            }
            item?.billerName.orEmpty().isNotEmpty() -> {
                binding.tvName.text = item?.billerName
                binding.llName.show()
            }
            else -> binding.llName.hide()
        }


        setupInitialViewData(item, binding)
        setupStatusColor(item?.statusId ?: 0, binding.tvStatus)

        binding.tvItemCount.text = (position + 1).toString()
        if(item?.providerName.orEmpty().isEmpty()){
            binding.llProvider.hide()
        }

        binding.btnCheckStatus.setOnClickListener { onCheckStatus?.invoke(item!!) }
        binding.btnPrint.setOnClickListener { onPrint?.invoke(item!!) }
        binding.btnComplain.setOnClickListener { onComplain?.invoke(item!!) }

    }

    private fun setupInitialViewData(item: LedgerReport?, binding: ListLedgerReport2Binding) {


        item?.let {

            if (item.isExpanded) {
                binding.llExpandedLayout.show()

            } else {
                binding.llExpandedLayout.hide()
            }

            binding.llBankName.isVisible(it.bankName)
            binding.llBeneficiary.isVisible(it.beneName)
            binding.llIfsc.isVisible(it.ifsc)
            binding.llOperatorId.isVisible(it.operatorId)
            binding.llRemark.isVisible(it.remark)
            binding.llMobile.isVisible(it.senderNumber)
            binding.llTxnType.isVisible(it.txnType)
            binding.llMode.isVisible(it.mode)

            if (it.isComplain)
                binding.btnComplain.show()
            else binding.btnComplain.hide()

            if (it.isPrint)
                binding.btnPrint.show()
            else binding.btnPrint.hide()

            if (it.isCheckStatus)
                binding.btnCheckStatus.show()
            else binding.btnCheckStatus.hide()


        }
    }

    private fun setupExpandedLayout(item: LedgerReport, binding: ListLedgerReport2Binding) {

        if (item.isExpanded) {
            binding.llExpandedLayout.hide()
            if(item.isPrint)binding.btnPrint.show()
        } else {
            binding.llExpandedLayout.show()
            binding.btnPrint.hide()
        }
        item.isExpanded = !item.isExpanded
    }

    private fun setupStatusColor(statusId: Int, textView: TextView) {
        val color = when (statusId) {
            1 -> R.color.success
            2 -> R.color.red
            3 -> R.color.yellow_dark
            else -> R.color.color_primary_dark
        }
        textView.setupTextColor(color)
    }

    var onPrint: ((item: LedgerReport) -> Unit)? = null
    var onComplain: ((item: LedgerReport) -> Unit)? = null
    var onCheckStatus: ((item: LedgerReport) -> Unit)? = null

}


private fun LinearLayout.isVisible(value: String?) {
    when {
        value == null -> {
            this.hide()
        }
        value.trim().isEmpty() -> {
            this.hide()
        }
        value.trim().equals("N/A", ignoreCase = true) -> {
            this.hide()
        }
        else -> this.show()
    }
}