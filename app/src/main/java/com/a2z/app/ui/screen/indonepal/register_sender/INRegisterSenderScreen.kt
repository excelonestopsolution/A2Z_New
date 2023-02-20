package com.a2z.app.ui.screen.indonepal.register_sender

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.NavTopBar


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun INRegisterSenderScreen() {
    val viewModel : INRegisterSenderViewModel = hiltViewModel()

    Scaffold(
        topBar = { NavTopBar(title = "Register Sender")}
    ) {

    }
}