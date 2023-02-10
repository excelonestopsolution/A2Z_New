package com.a2z.app.activity.login


import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.a2z.app.AppPreference
import com.a2z.app.R
import com.a2z.app.activity.ForgetPasswordActivity
import com.a2z.app.activity.MainActivity
import com.a2z.app.activity.login_id.forgot.ForgotLoginIdActivity
import com.a2z.app.activity.register.RegistrationActivity
import com.a2z.app.databinding.ActivityLogin2Binding
import com.a2z.app.util.*
import com.a2z.app.util.apis.Resource
import com.a2z.app.util.dialogs.StatusDialog
import com.a2z.app.util.ents.handleNetworkFailure
import com.a2z.app.util.ents.launchIntent
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val REQUEST_CODE_APP_UPDATE = 102

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), InstallStateUpdatedListener {

    private val mLocationManager: LocationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }


    //APP UPDATE VARIABLES
    private lateinit var appUpdateManager: AppUpdateManager

    private var forceUpdate: Boolean = true
    private var shouldUpdate: Boolean = true

    private lateinit var binding: ActivityLogin2Binding
    val viewModel: LoginViewModel by viewModels()

    var isLocationServiceEnable  = false


    @Inject
    lateinit var appPreference: AppPreference


    private var progressDialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnLogin.setOnClickListener {

            loginButtonClick()
            //onLoginButtonClick()
        }

        binding.btnSignup.setOnClickListener {

            if (viewModel.location == null) {
                fetchLocation(onStart = {
                    progressDialog = StatusDialog.progress(this, "Validating Location")
                }) {
                    progressDialog?.dismiss()
                    viewModel.location = it
                    launchIntent(
                        RegistrationActivity::class.java, bundleOf(
                            AppConstants.IS_SELF_REGISTRATION to true
                        )
                    )
                }
            } else {

                launchIntent(
                    RegistrationActivity::class.java, bundleOf(
                        AppConstants.IS_SELF_REGISTRATION to true
                    )
                )
            }


        }

        binding.btnForgotPassword.setOnClickListener {

            if (viewModel.location == null) {
                fetchLocation(onStart = {
                    progressDialog = StatusDialog.progress(this, "Validating Location")
                }) {
                    progressDialog?.dismiss()
                    viewModel.location = it
                    forgotBottomSheetDialog()
                }
            } else {
                forgotBottomSheetDialog()
            }

        }

        subscribeObserver()

        setupLoginChecked()

        fetchLocation()

        ImagePickerActivity

        ViewUtil.resetErrorOnTextInputLayout(binding.tilMobileNumber, binding.tilPassword)
    }

    private  fun loginButtonClick(){
        if (!validateInputs()) return
        if (viewModel.location == null) {
            fetchLocation(onStart = {
                progressDialog = StatusDialog.progress(this, "Validating Location")
            }) {
                progressDialog?.dismiss()
                viewModel.location = it
                onLoginButtonClick()
            }
        } else onLoginButtonClick()
    }
    private fun showRetryMessage(){
        StatusDialog.showRetryMessage(this,"May be Internet Connection issue \n\nPlease Retry Again",DialogInterface.OnClickListener { dialogInterface, i ->
            when (i) {
                DialogInterface.BUTTON_POSITIVE -> {
                    loginButtonClick()
                }
                DialogInterface.BUTTON_NEGATIVE -> {

                }
            }
        })
    }
    private fun forgotBottomSheetDialog() {

        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogStyle)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_forgot)

        bottomSheetDialog.findViewById<TextView>(R.id.tv_forgot_password)?.setOnClickListener {
            bottomSheetDialog.dismiss()
            launchIntent(ForgetPasswordActivity::class.java)
        }

        bottomSheetDialog.findViewById<TextView>(R.id.tv_forgot_username)?.setOnClickListener {
            bottomSheetDialog.dismiss()
            launchIntent(ForgotLoginIdActivity::class.java)
        }

        bottomSheetDialog.show()


    }


    private fun subscribeObserver() {

        viewModel.loginObs.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    progressDialog = StatusDialog.progress(this, "Login")
                }
                is Resource.Success -> {
                    progressDialog?.dismiss()
                    if (it.data.status == 1) {

                        if (it.data.role_id == 5
                            || it.data.role_id == 4
                            || it.data.role_id == 3
                            || it.data.role_id == 22
                            || it.data.role_id == 23
                            || it.data.role_id == 24
                        ) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else StatusDialog.failure(
                            this,
                            "Sorry App is available only for your role id"
                        )

                    } else if (it.data.status == 108) {
                        val dialog1: Dialog = AppDialogs.transactionStatus(this, it.data.message, 1)
                        val bntOk = dialog1.findViewById<Button>(R.id.btn_ok)
                        bntOk.setOnClickListener { _ ->
                            dialog1.dismiss()
                            val intent = Intent(this, ForgetPasswordActivity::class.java)
                            intent.putExtra("mobile", viewModel.loginId)
                            startActivity(intent)
                        }
                        dialog1.show()
                    } else if (it.data.status == 700) {
                        val intent = Intent(this, DeviceVerificationActivity::class.java)
                        intent.putExtra("mobile_number", viewModel.loginId)
                        startActivity(intent)
                    } else StatusDialog.failure(this, it.data.message)
                }
                is Resource.Failure -> {
                    progressDialog?.dismiss()
                    handleNetworkFailure(it.exception)
                }

                is Resource.FailureWithOtherProblem ->{
                    progressDialog?.dismiss()
                    this.showRetryMessage()
                }
            }
        }

    }

    private fun setupLoginChecked() {


        if (appPreference.autoLogin) {

            try {
                viewModel.loginId = AppSecurity.decrypt(appPreference.loginID) ?: ""
                viewModel.password = AppSecurity.decrypt(appPreference.loginPassword) ?:""
            } catch (e: Exception) {
                viewModel.loginId = ""
                viewModel.password = ""
            }


        } else {
            appPreference.deleteMobile()
            appPreference.deleteLoginPassword()

            binding.run {
                edMobileNumber.setText("")
                edPassword.setText("")
            }
        }
    }

    private fun onLoginButtonClick() {
        viewModel.imei = AppUitls.id(this)
        viewModel.login()
    }

    private fun validateInputs(): Boolean {
        var isValidate: Boolean = true

        if (viewModel.loginId.length !in 6..10) {
            binding.tilMobileNumber.apply {
                error = "Enter valid user id"
                isErrorEnabled = true
            }
            isValidate = false
        } else binding.tilMobileNumber.isErrorEnabled = false

        if (viewModel.password.length < 6) {
            binding.tilPassword.apply {
                error = "Enter minimum 6 characters password"
                isErrorEnabled = true
            }
            isValidate = false
        } else binding.tilPassword.isErrorEnabled = false


        return isValidate
    }

    ///////////////////////////////////////===APP UPDATE FUNCATIONALITY===///////////////////////


    override fun onResume() {
        super.onResume()
        if (shouldUpdate)
            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    showSnackbar(
                        getString(R.string.app_update_msg),
                        getString(R.string.restart),
                        onClickListener
                    )
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    requestUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE)
                }
            }
    }

    private fun requestUpdate(
        appUpdateInfo: AppUpdateInfo?,
        updateType: Int = AppUpdateType.IMMEDIATE
    ) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo!!,
            updateType,
            this@LoginActivity,
            REQUEST_CODE_APP_UPDATE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_APP_UPDATE && resultCode != RESULT_OK) {

            if (forceUpdate) {
                Toast.makeText(
                    this,
                    "There was some major changes on this app, need update first to to run",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            showSnackbar(
                getString(R.string.app_update_msg),
                getString(R.string.restart),
                onClickListener
            )
            appUpdateManager.unregisterListener(this)
        }
    }

    private fun showSnackbar(
        message: String,
        actionText: String,
        clickListener: View.OnClickListener
    ) {

        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(actionText, clickListener).show()

    }

    private val onClickListener = View.OnClickListener { view ->
        appUpdateManager.completeUpdate()
    }
    private fun fetchLocation(onStart: () -> Unit = {}, onFetch: (Location?) -> Unit = {}) {
        if (viewModel.location != null) {
            onFetch(viewModel.location)
        } else {
            isLocationServiceEnable = LocationService.isLocationEnabled(this)
            Log.v("isLocationServiceEnable",""+isLocationServiceEnable)
            if (isLocationServiceEnable) {
                requestForLocationPhoneReadState {
                    onStart()
                    LocationService.getCurrentLocation(mLocationManager)
                    LocationService.setupListener(object : LocationService.MLocationListener {
                        override fun onLocationChange(location: Location) {
                            if (viewModel.location == null) {
                                globalLocation = location
                                viewModel.location = location
                                AppConstants.userLocation = location
                                Log.v("Location",""+location.latitude+ ""+ location.longitude)
                                onFetch(location)
                            }
                        }

                        override fun onLocationGetNillValue() {
                            progressDialog?.dismiss()
                            //this@LoginActivity.handleNetworkFailure(Exceptions.NoInternetException("Network connection is too slow, please try again after sometime"))
                            fetchLocation()
                        }
                    })
                }
            }
            else{
                progressDialog?.dismiss()
            }

        }
    }

}
