package com.a2z.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a2z.di.R
import com.a2z.app.dist.data.preference.AppPreference
import com.a2z.di.databinding.ActivityExceptionBinding
import com.a2z.app.util.AppConstants
import com.a2z.app.util.apis.Exceptions
import com.a2z.app.util.ents.goToMainActivity
import com.a2z.app.util.ents.gotoLoginActivity
import com.a2z.app.util.ents.set
import com.a2z.app.util.enums.LottieType
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import kotlin.system.exitProcess


@AndroidEntryPoint
class ExceptionActivity : AppCompatActivity() {

    @Inject
    lateinit var appPreference: AppPreference
    private lateinit var binding: ActivityExceptionBinding

    private lateinit var exception: Exception
    private var shouldBack: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExceptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exception = intent.extras?.getSerializable(AppConstants.EXCEPTION) as Exception
        shouldBack = intent.getBooleanExtra(AppConstants.ABLE_TO_BACK, false)



        if (appPreference.doubleTxn.isException)
            setupTransaction()
        else setupException()


    }

    private fun setupTransaction() {
        appPreference.doubleTxn = appPreference.doubleTxn.copy(isException = false)
        shouldBack = false
        binding.run {
            lottieView.set(LottieType.PENDING)
            tvTitle.text = getString(R.string.transaction_in_pending)
            tvDescription.text = getString(R.string.transaction_interrupted_message)
        }

    }

    private fun setupException() {
        when (exception) {

            is TimeoutException , is SocketTimeoutException-> {
                binding.run {
                    lottieView.set(LottieType.TIME_OUT)
                    tvTitle.text = "Time Out"
                    tvDescription.text = "Network connection is too slow, please try again after sometime"

                }
            }

            is Exceptions.NoInternetException -> {
                binding.run {
                    lottieView.set(LottieType.NO_INTERNET)
                    tvTitle.text = getString(R.string.no_internet)
                   // tvDescription.text = "Network connection is too slow, please try again after sometime"
                    tvDescription.text = "Please check your mobile data & Internet settings."
                }
            }
            is Exceptions.UnAuthorizedException -> {
                shouldBack = false
                binding.run {
                    lottieView.set(LottieType.ALERT)
                    tvTitle.text = getString(R.string.unauthorized_access)
                    tvDescription.text = exception.message.toString()

                }
            }
            is Exceptions.InternalServerError -> {
                binding.run {
                    lottieView.set(LottieType.SERVER)
                    tvTitle.text = getString(R.string.internal_server_error)
                    tvDescription.text = "Please try after sometime"

                }
            }
            is Exceptions.RootException -> {
                binding.run {
                    lottieView.set(LottieType.ALERT)
                    tvTitle.text = exception.message.toString()
                    tvDescription.text = getString(R.string.root_message)

                }
            }
            is Exceptions.SessionExpiredException -> {
                binding.run {
                    lottieView.set(LottieType.ALERT)
                    tvTitle.text = "Session Expired"
                    tvDescription.text = exception.message.toString()
                }
            }
            is Exceptions.AppInProgressException -> {
                binding.run {
                    lottieView.set(LottieType.ALERT)
                    tvTitle.text = "App In Progress"
                    tvDescription.text = exception.message.toString()

                }
            }
            else -> {
                var message = exception.message.toString()
                if(message.contains("partners.a2zsuvidhaa.com")){
                    message = "Network connection is too slow, please try again after sometime"
                }

                binding.run {
                    lottieView.set(LottieType.ALERT)
                    tvTitle.text = "Connection terminate!"
                    //tvDescription.text =message
                    tvDescription.text ="Network connection is too slow, please try again" // change by Surender
                }
            }

        }
    }


    override fun onBackPressed() {

        when {
            exception is Exceptions.RootException -> {
                finish()
                exitProcess(0)
            }
            exception is Exceptions.SessionExpiredException -> {
                gotoLoginActivity()
            }

            exception is Exceptions.AppInProgressException -> {
                gotoLoginActivity()
            }
            shouldBack -> super.onBackPressed()
            else -> goToMainActivity()
        }
    }


}