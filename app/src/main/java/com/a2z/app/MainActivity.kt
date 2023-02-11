package com.a2z.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.a2z.app.data.local.AppPreference
import com.a2z.app.service.location.LocationService
import com.app.mylibrary.TestActivity
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class InitialRouteType {
    LOGIN_PAGE, DASHBOARD_PAGE, DEVICE_LOCK_PAGE
}

@AndroidEntryPoint
class MainActivity : FragmentActivity(), PaymentResultWithDataListener {

    @Inject
    lateinit var locationService: LocationService

    @Inject
    lateinit var appPreference: AppPreference

    private val viewModel: MainViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        appPreference.latitude = ""
        appPreference.longitude = ""

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value ?: false
            }
        }

        Intent(this, TestActivity::class.java).apply {
            startActivity(this)
        }

        /*  setContent {

              CompositionLocalProvider(
                  LocalNavController provides rememberNavController(),
                  LocalLocationService provides locationService
              ) {
                  A2ZApp {
                      ProvideWindowInsets(
                          windowInsetsAnimationsEnabled = true,
                          consumeWindowInsets = false
                      ) {

                          var initialRouteType = InitialRouteType.LOGIN_PAGE
                          *//*if (!LocalAuth.checkForBiometrics(this))
                            initialRouteType = InitialRouteType.DEVICE_LOCK_PAGE
*//*
                        MainNav(viewModel, initialRouteType)

                    }
                }
            }

        }*/
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, data: PaymentData?) {
        lifecycleScope.launch {
            viewModel.pgResult.emit(PGResultType.Success(data, razorpayPaymentId))
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?, data: PaymentData?) {
        lifecycleScope.launch {
            viewModel.pgResult.emit(PGResultType.Failure(data, errorCode, response))
        }
    }


}

class MainViewModel : ViewModel() {
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    val pgResult = MutableSharedFlow<PGResultType>()

    init {
        viewModelScope.launch {
            delay(10)
            _isLoading.value = false
        }
    }
}

sealed class PGResultType {
    object Nothing : PGResultType()
    class Success(val data: PaymentData?, val razorpayPaymentId: String?) : PGResultType()
    data class Failure(val data: PaymentData?, val errorCode: Int, val response: String?) :
        PGResultType()
}






