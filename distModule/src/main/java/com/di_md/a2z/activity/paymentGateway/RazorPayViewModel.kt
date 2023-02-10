package com.di_md.a2z.activity.paymentGateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.di_md.a2z.AppPreference
import com.di_md.a2z.BaseViewModel
import com.di_md.a2z.data.repository.PaymentgatewayNTicketRepository
import com.di_md.a2z.model.pg.RzpayOrderAckDataModel
import com.di_md.a2z.util.AppLog
import com.di_md.a2z.util.apis.Resource
import com.di_md.a2z.util.apis.SingleMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RazorPayViewModel  @Inject constructor(val rajorPayRepository: PaymentgatewayNTicketRepository,
                                             val appPreference: AppPreference): BaseViewModel() {

    var amount: String = ""
    var mobileNumber: String = ""
    var  ackNumber: String = ""


    private val _orderInfo = SingleMutableLiveData<Resource<RzpayOrderAckDataModel>>()
    val orderInfo: LiveData<Resource<RzpayOrderAckDataModel>> = _orderInfo

    fun  getOrderNumber(amount: Long,mobileNo: Long){
        _orderInfo.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiRequest {
                        rajorPayRepository.getOrderAckNumber(mobileNo,amount)
                    }
                }
//                if (response.status == 1) {
//                    //saveLoginData(response)
//
//                }
                _orderInfo.value = Resource.Success(response)
            } catch (e: Exception) {
                AppLog.d("exception_test : ${e.printStackTrace()}")
                _orderInfo.value = Resource.Failure(e)
            }
        }
    }




}