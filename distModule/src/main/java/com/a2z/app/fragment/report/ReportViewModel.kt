package com.a2z.app.fragment.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.a2z.app.AppPreference
import com.a2z.app.BaseViewModel
import com.a2z.app.model.TransactionDetailResponse
import com.a2z.app.model.report.ComplainType
import com.a2z.app.model.report.ComplainTypeListResponse
import com.a2z.app.model.report.LedgerReport
import com.a2z.app.dist.data.repository.ReportRepository
import com.a2z.app.model.CommonResponse
import com.a2z.app.util.AppConstants
import com.a2z.app.util.AppLog
import com.a2z.app.util.DateUtil
import com.a2z.app.util.apis.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val appPreference: AppPreference
) : BaseViewModel() {

    var fromDate = DateUtil.previousDate(6)
    var toDate = DateUtil.currentDate()
    var selectedProduct = ""
    var selectedStatus = ""
    var selectedCriteria = ""
    var searchInput = ""


    fun fetchLedgerReport(): Flow<PagingData<LedgerReport>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
            ),
            pagingSourceFactory = {
                LedgerPagingSource(
                    reportRepository,
                    hashMapOf(
                        "fromdate" to fromDate,
                        "todate" to toDate,
                        "searchType" to selectedCriteria,
                        "number" to searchInput,
                        "status_id" to selectedStatus,
                        "product" to selectedProduct,
                    )
                )
            }
        ).flow
    }


    private val _checkStatusObs = MutableLiveData<Resource<CommonResponse>>()
    val checkStatusObs : LiveData<Resource<CommonResponse>> = _checkStatusObs

    fun checkStatus(id: String) {
        viewModelScope.launch {
           _checkStatusObs.value = Resource.Loading()
            try {
                val response = withContext(Dispatchers.IO){
                    reportRepository.checkStatus(hashMapOf(
                        "id" to id,
                        "userId" to appPreference.id.toString(),
                        "token" to appPreference.token,
                    ))
                }
                _checkStatusObs.value = Resource.Success(response)
            }catch (e: Exception){
                _checkStatusObs.value = Resource.Failure(e)
            }

        }
    }

    private val _complainTypes = MutableLiveData<Resource<ComplainTypeListResponse>>()
    val complainTypes : LiveData<Resource<ComplainTypeListResponse>> = _complainTypes

    fun fetchComplainTypes(id : String) {
        viewModelScope.launch {
            _complainTypes.value = Resource.Loading()
            try {
                val response = withContext(Dispatchers.IO){
                    reportRepository.fetchComplainTypes(hashMapOf(
                        "userId" to appPreference.id.toString(),
                        "token" to appPreference.token.toString(),
                        "transaction_type_id" to id,

                    ))
                }
                _complainTypes.value = Resource.Success(response)
            }catch (e: Exception){
                _complainTypes.value = Resource.Failure(e)
            }

        }
    }


    var transactionId = ""
    private val _complainObs = MutableLiveData<Resource<CommonResponse>>()
    val complainObs : LiveData<Resource<CommonResponse>> = _complainObs

    fun makeComplain(reason: ComplainType,remark :String) {
        viewModelScope.launch {
            _complainObs.value = Resource.Loading()
            try {
                val response = withContext(Dispatchers.IO){
                    reportRepository.makeComplain(hashMapOf(
                        "userId" to appPreference.id.toString(),
                        "token" to appPreference.token.toString(),
                        "transaction_id" to transactionId,
                        "issueType" to reason.id,
                        "remark" to remark,
                    ))
                }
                _complainObs.value = Resource.Success(response)
            }catch (e: Exception){
                _complainObs.value = Resource.Failure(e)
            }

        }
    }


    private val _downloadReceiptObs = MutableLiveData<Resource<TransactionDetailResponse>>()
    val downloadReceiptObs : LiveData<Resource<TransactionDetailResponse>> = _downloadReceiptObs

    fun fetchReceiptData(id : String) {
        viewModelScope.launch {
            _downloadReceiptObs.value = Resource.Loading()
            try {
                val response = withContext(Dispatchers.IO){
                    reportRepository.downloadLedgerReceiptData(AppConstants.BASE_URL+"slip/new/${id}")
                }.body()!!
                AppLog.d("Receipt data : $response")
                _downloadReceiptObs.value = Resource.Success(response)
            }catch (e: Exception){
                _downloadReceiptObs.value = Resource.Failure(e)
            }
        }
    }


}