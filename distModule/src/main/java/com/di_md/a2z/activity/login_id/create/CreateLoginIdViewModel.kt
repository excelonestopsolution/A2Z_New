package com.di_md.a2z.activity.login_id.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.di_md.a2z.AppPreference
import com.di_md.a2z.BaseViewModel
import com.di_md.a2z.data.repository.AuthRepository
import com.di_md.a2z.data.response.CommonResponse
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
class CreateLoginIdViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val appPreference: AppPreference
) : BaseViewModel() {

    var loginId = ""
    var confirmLoginId = ""
    var otp = ""

    private val _createLoginIdObs = SingleMutableLiveData<Resource<CommonResponse>>()
    val createLoginIdObs: LiveData<Resource<CommonResponse>> = _createLoginIdObs

    fun createLoginId() {
        _createLoginIdObs.value = Resource.Loading()

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiRequest {
                        authRepository.createLoginId(
                            hashMapOf(
                                "login_id" to loginId,
                                "confirm_login_id" to confirmLoginId,
                            )
                        )
                    }
                }
                _createLoginIdObs.value = Resource.Success(response)
            } catch (e: Exception) {
                AppLog.d("exception_test : ${e.printStackTrace()}")
                _createLoginIdObs.value = Resource.Failure(e)
            }
        }
    }








    private val _onVerifyOtp = MutableLiveData<Resource<CommonResponse>>()
    val onVerifyOtp: LiveData<Resource<CommonResponse>> = _onVerifyOtp


    fun verifyLoginId() {
        _onVerifyOtp.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiRequest {
                         authRepository.verifyLoginId(
                             hashMapOf(
                                 "login_id" to loginId,
                                 "loginIdOtp" to otp
                             )
                         )

                    }
                }
                _onVerifyOtp.value = Resource.Success(response)
            } catch (e: Exception) {
                _onVerifyOtp.value = Resource.Failure(e)
            }
        }
    }

}