package com.a2z.app.fragment.report

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.a2z.app.AppPreference
import com.a2z.di.R
import com.a2z.app.activity.DistAepsResultActivity
import com.a2z.app.activity.DistDmtResultActivity
import com.a2z.app.activity.DistUpiResultActivity
import com.a2z.app.activity.matm.MatmResponseActivity
import com.a2z.app.activity.recharge.BillRechargeResultActivity
import com.a2z.app.adapter.report.LedgerPagingAdapter
import com.a2z.app.model.report.LedgerReport
import com.a2z.di.databinding.FragmentLedgreReportBinding
import com.a2z.app.fragment.BaseFragment
import com.a2z.app.util.AppConstants
import com.a2z.app.util.DateUtil
import com.a2z.app.util.ViewUtil
import com.a2z.app.util.apis.Resource
import com.a2z.app.util.dialogs.LedgerFilterDialog
import com.a2z.app.util.dialogs.StatusDialog
import com.a2z.app.util.ents.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class LedgerReportFragment : BaseFragment<FragmentLedgreReportBinding>(R.layout.fragment_ledgre_report) {

    @Inject
    lateinit var appPreference: AppPreference

    private var adapter: LedgerPagingAdapter? = null

    companion object {
        fun newInstance() = LedgerReportFragment()
    }

    private val viewModel: ReportViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fetchLedgerReport()

        binding.swipeView.setOnRefreshListener {
            viewModel.fromDate = DateUtil.previousDate(6)
            viewModel.toDate = DateUtil.currentDate()
            viewModel.selectedProduct = ""
            viewModel.selectedCriteria = ""
            viewModel.selectedStatus = ""
            fetchLedgerReport()
        }

        subscribeObservers()

        binding.fabFilter.setOnClickListener {
            LedgerFilterDialog(
                    status = viewModel.selectedStatus,
                    fromDate = viewModel.fromDate,
                    toDate = viewModel.toDate,
                    product = viewModel.selectedProduct,
                    criteria = viewModel.selectedCriteria,
                    inputNumber = viewModel.searchInput

            ).showDialog(
                    requireActivity(),
                    onSearchClick = { fromDate, toDate, product, status, criteria, input ->
                        viewModel.fromDate = fromDate
                        viewModel.toDate = toDate
                        viewModel.selectedProduct = product
                        viewModel.selectedStatus = status
                        viewModel.selectedCriteria = criteria
                        viewModel.searchInput = input
                        fetchLedgerReport()
                    }
            )
        }

        ViewUtil.extendFab(binding.recyclerView, binding.fabFilter)
    }


    private fun fetchLedgerReport() {
        adapter = LedgerPagingAdapter()
        binding.recyclerView.setup().adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel.fetchLedgerReport().collectLatest {
                adapter?.submitData(it)
            }
        }

        adapter?.addLoadStateListener { loadStates ->
            when (loadStates.source.refresh) {
                is LoadState.NotLoading -> {
                    binding.swipeView.isRefreshing = false
                    binding.shimmerViewContainer.hide()
                    binding.shimmerViewContainer.stopShimmerAnimation()
                    binding.fabFilter.show()

                    if(adapter!!.itemCount >0){
                        binding.layoutNoItemFound.hide()
                    }
                    else binding.layoutNoItemFound.show()
                }
                is LoadState.Loading -> {
                    binding.layoutNoItemFound.hide()
                    binding.fabFilter.hide()
                    binding.shimmerViewContainer.show()
                    binding.shimmerViewContainer.startShimmerAnimation()
                }
                is LoadState.Error -> {
                }

            }
        }

        adapter?.onPrint = {
            downloadReceipt(it)
        }
        adapter?.onCheckStatus = {

            val id : String = it.id.toString()
            viewModel.checkStatus(id)
        }
        adapter?.onComplain = {

            viewModel.transactionId = it.id.toString()
            viewModel.fetchComplainTypes(it.transactionTypeId.toString())
        }
    }

    private fun downloadReceipt(ledgerReport : LedgerReport){

        viewModel.fetchReceiptData(ledgerReport.id.toString())

    }

    private fun subscribeObservers() {


        viewModel.complainTypes.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading ->{
                    progressDialog = StatusDialog.progress(requireContext())
                }
                is Resource.Success ->{
                    progressDialog?.dismiss()
                    if(it.data.status == 1){
                        LedgerReportComponent(requireContext(),it.data.data) { complainType, remark ->
                            viewModel.makeComplain(complainType, remark)
                        }
                    }
                    else StatusDialog.failure(requireActivity(),it.data.message)
                }
                is Resource.Failure ->{
                    progressDialog?.dismiss()
                    activity?.handleNetworkFailure(it.exception)
                }
                else -> {}
            }
        }

        viewModel.checkStatusObs.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    progressDialog = StatusDialog.progress(requireContext())
                }
                is Resource.Success -> {
                    progressDialog?.dismiss()
                    when (it.data.status) {
                        1 -> StatusDialog.success(requireActivity(), it.data.message)
                        3 ->
                            StatusDialog.pending(requireActivity(), it.data.message)
                        else -> StatusDialog.failure(requireActivity(), it.data.message)
                    }
                }
                is Resource.Failure -> {
                    progressDialog?.dismiss()
                    activity?.handleNetworkFailure(it.exception)
                }
                else -> {}
            }
        }

        viewModel.complainObs.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    progressDialog = StatusDialog.progress(requireContext())
                }
                is Resource.Success -> {
                    progressDialog?.dismiss()
                    if (it.data.status == 1)
                        StatusDialog.success(requireActivity(), it.data.message)
                    else StatusDialog.failure(requireActivity(), it.data.message)
                }
                is Resource.Failure -> {
                    progressDialog?.dismiss()
                    activity?.handleNetworkFailure(it.exception)
                }
                else -> {}
            }
        }

        viewModel.downloadReceiptObs.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    progressDialog = StatusDialog.progress(requireContext())
                }
                is Resource.Success -> {
                    progressDialog?.dismiss()
                    if (it.data.status == 1) {


                        when (it.data.data?.slipType) {
                            "TRANSACTION" -> {
                                activity?.launchIntent(
                                        DistDmtResultActivity::class.java,
                                        data = bundleOf(
                                                AppConstants.DATA_OBJECT to it.data.data,
                                                AppConstants.ORIGIN to AppConstants.REPORT_ORIGIN
                                        )
                                )
                            }
                            "BILLPAYMENT", "RECHARGE", "FASTTAG" -> {
                                activity?.launchIntent(BillRechargeResultActivity::class.java, bundleOf(
                                        AppConstants.DATA_OBJECT to it.data.data,
                                        AppConstants.ORIGIN to AppConstants.REPORT_ORIGIN
                                ))
                            }
                            "AEPS" -> {
                                activity?.launchIntent(DistAepsResultActivity::class.java, bundleOf(
                                        AppConstants.DATA_OBJECT to it.data.data,
                                        AppConstants.ORIGIN to AppConstants.REPORT_ORIGIN,
                                        AppConstants.FROM_REPORT to AppConstants.LEDGER_REPORT
                                ))
                            }
                            "UPI_PAYMENT" -> {
                                activity?.launchIntent(
                                        DistUpiResultActivity::class.java,
                                        data = bundleOf(
                                                AppConstants.DATA_OBJECT to it.data.data,
                                                AppConstants.ORIGIN to AppConstants.REPORT_ORIGIN
                                        )
                                )
                            }
                            "MATM"->{
                                activity?.launchIntent(
                                    MatmResponseActivity::class.java,
                                    data = bundleOf(
                                        AppConstants.DATA_OBJECT to it.data.data,
                                        AppConstants.ORIGIN to AppConstants.REPORT_ORIGIN,
                                        AppConstants.REPORT_ORIGIN to AppConstants.LEDGER_REPORT
                                    )
                                )
                            }
                        }

                    } else StatusDialog.failure(requireActivity(), it.data.message.orEmpty())
                }
                is Resource.Failure -> {
                    progressDialog?.dismiss()
                    activity?.handleNetworkFailure(it.exception)
                }
                else -> {}
            }
        }
    }

}