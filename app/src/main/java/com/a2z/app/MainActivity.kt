package com.a2z.app

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.a2z.app.data.local.AppPreference
import com.a2z.app.nav.MainNav
import com.a2z.app.service.LocalAuth
import com.a2z.app.service.location.LocationService
import com.a2z.app.ui.theme.A2ZApp
import com.a2z.app.ui.theme.LocalLocationService
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.util.extension.showToast
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class InitialRouteType {
    LOGIN_PAGE, DASHBOARD_PAGE, DEVICE_LOCK_PAGE
}

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var locationService: LocationService

    @Inject
    lateinit var appPreference: AppPreference

    private val viewModel: MainViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value ?: false
            }
        }
        setContent {

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
                        if (appPreference.user?.token != null)
                            initialRouteType = InitialRouteType.DASHBOARD_PAGE
                        if (!LocalAuth.checkForBiometrics(this))
                            initialRouteType = InitialRouteType.DEVICE_LOCK_PAGE

                        MainNav(viewModel, initialRouteType)
                    }
                }
            }

        }
    }

}

class MainViewModel : ViewModel() {
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            delay(10)
            _isLoading.value = false
        }
    }

}



