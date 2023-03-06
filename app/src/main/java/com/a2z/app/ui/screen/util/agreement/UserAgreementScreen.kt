package com.a2z.app.ui.screen.util.agreement

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.data.model.AgreementInitialInfo
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.EmailTextField
import com.a2z.app.ui.component.common.MobileTextField
import com.a2z.app.ui.component.permission.PermissionComponent
import com.a2z.app.ui.screen.util.permission.AppPermissionList
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.RedColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
@Composable
fun UserAgreementScreen() {

    val viewModel: UserAgreementViewModel = hiltViewModel()

    Scaffold(backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "User Agreement") }) {
        BaseContent(viewModel) {

            ObsComponent(flow = viewModel.initialAgreementDetailResultFlow) {
                when (it.status) {
                    1 -> AgreementStatusContent(
                        viewModel,
                        it.message.toString(),
                        it.data?.agreementUrl.toString()
                    )
                    3 -> AgreementFormContent(viewModel, it.data)

                    else -> viewModel.failureDialog(it.message.toString()) {
                        viewModel.navigateUpWithResult()
                    }

                }
            }


        }

        val context = LocalContext.current
        LaunchedEffect(key1 = Unit, block = {
            viewModel.startBrowserIntent.collectLatest {
                if (!it) return@collectLatest
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.intentUrl))
                context.startActivity(browserIntent)
            }
        })
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun AgreementWebViewContent(viewModel: UserAgreementViewModel) {

    LaunchedEffect(key1 = Unit, block = {
        viewModel.progressDialog("Loading Content...")
    })

    fun WebView.webViewParam() {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    fun WebView.webViewClient() {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val script2 = """
                         document.querySelector("input[name=checkbox]").addEventListener('change', function() {
                      if (this.checked) {
                        AgreementCallback.onAgree("true");
                      } else {
                        AgreementCallback.onAgree("false");
                      }
                    });
                       """.trimIndent()
                evaluateJavascript(script2, null)
                viewModel.dismissDialog()
            }
        }
    }

    fun WebView.webViewInterfaceCallback(
        scope: CoroutineScope,
        viewModel: UserAgreementViewModel
    ) {
        addJavascriptInterface(
            AgreementCallback {
                scope.launch {
                    withContext(Dispatchers.Main) {
                        if (it) viewModel.buttonState.value = true
                        else viewModel.buttonState.value = true
                    }
                }
            }, "AgreementCallback"
        )
    }


    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val scope = rememberCoroutineScope()

            BoxWithConstraints(
                Modifier
                    .weight(1f)
                    .background(RedColor)
                    .fillMaxWidth()
            ) {
                AndroidView(factory = {
                    WebView(it).apply {
                        webViewParam()
                        settings.javaScriptEnabled = true
                        webViewClient()
                        webViewInterfaceCallback(scope, viewModel)
                    }
                }, update = {
                    it.loadUrl(
                        "https://partners.a2zsuvidhaa.com/mobileapp/api/agreement/content",
                        hashMapOf(
                            "user-id" to viewModel.user?.id.toString(),
                            "token" to viewModel.user?.token.toString()
                        )
                    )
                }, modifier = Modifier
                    .width(maxWidth)
                    .height(maxHeight)
                )
            }

            Button(
                enabled = viewModel.buttonState.value,
                onClick = { viewModel.onUserAcceptAndProceed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(text = "Accept & Proceed")
            }
        }

    }
}


@Composable
private fun AgreementFormContent(viewModel: UserAgreementViewModel, data: AgreementInitialInfo?) {

    if (viewModel.showWebViewContentState.value)
        AgreementWebViewContent(viewModel)
    else Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {


        val input = viewModel.input
        input.name.setValue(data?.name)
        input.email.setValue(data?.email)
        input.mobile.setValue(data?.mobile)

        Column(modifier = Modifier.padding(16.dp)) {

            AppTextField(
                value = input.name.getValue(),
                label = "Full Name",
                onChange = { input.name.onChange(it) },
                downText = "User full name",
                readOnly = true
            )
            EmailTextField(
                value = input.email.getValue(),
                onChange = { input.email.onChange(it) },
                downText = "eg:- abc@xyz.com",
                readOnly = true
            )
            MobileTextField(
                value = input.mobile.getValue(),
                onChange = { input.mobile.onChange(it) },
                readOnly = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onFormSubmit() }, modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        52.dp
                    )
            ) {
                Text("Submit")
            }

        }

    }
}

@Composable
private fun AgreementStatusContent(
    viewModel: UserAgreementViewModel,
    message: String,
    downloadUrl: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Agreement Status", style = MaterialTheme.typography.h6)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.icon_sucess),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(GreenColor)
                )
                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = message,
                    color = GreenColor, fontWeight = FontWeight.W400
                )
            }


            val context = LocalContext.current


            PermissionComponent(AppPermissionList.cameraStorages().map { it.permission }) {
                Button(
                    onClick = {
                        val result = it.invoke()
                        if (result) {
                            val downloadManager =
                                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                            viewModel.onDownloadAgreement(downloadUrl, downloadManager)
                        } else {
                            viewModel.navigateTo(
                                NavScreen.PermissionScreen.passData(
                                    permissionType = PermissionType.CameraAndStorage
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Download Agreement")
                }
            }


        }

    }
}