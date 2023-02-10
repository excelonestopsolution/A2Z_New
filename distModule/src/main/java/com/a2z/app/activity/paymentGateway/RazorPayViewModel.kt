package com.a2z.app.activity.paymentGateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.a2z.app.AppPreference
import com.a2z.app.BaseViewModel
import com.a2z.app.dist.data.repository.PaymentgatewayNTicketRepository
import com.a2z.app.model.pg.RzpayOrderAckDataModel
import com.a2z.app.util.AppLog
import com.a2z.app.util.apis.Resource
import com.a2z.app.util.apis.SingleMutableLiveData
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