package com.a2z.app

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.navigation.compose.rememberNavController
import com.a2z.app.data.local.AppPreference
import com.a2z.app.nav.MainNav
import com.a2z.app.nav.NavScreen
import com.a2z.app.service.LocalAuth
import com.a2z.app.service.location.LocationService
import com.a2z.app.ui.theme.A2ZApp
import com.a2z.app.ui.theme.LocalLocationService
import com.a2z.app.ui.theme.LocalNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



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

        setContent {

            CompositionLocalProvider(
                LocalNavController provides rememberNavController(),
                LocalLocationService provides locationService,) {
                A2ZApp {
                    ProvideWindowInsets(
                        windowInsetsAnimationsEnabled = true,
                        consumeWindowInsets = false
                    ) {
                        val context = LocalContext.current
                        var initialRoute = NavScreen.LoginScreen.route
                        if (appPreference.user != null
                            && LocalAuth.checkForBiometrics(context)
                        ) initialRoute = NavScreen.DashboardScreen.route
                        MainNav(viewModel, initialRoute)
                    }
                }
            }

        }
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






