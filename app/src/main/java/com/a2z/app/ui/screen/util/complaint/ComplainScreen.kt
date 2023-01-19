package com.a2z.app.ui.screen.util.complaint

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ComplaintScreen() {

    val viewModel: ComplainViewModel = hiltViewModel()

    Scaffold(topBar = { NavTopBar(title = "Complaints") }) {
        BaseContent(viewModel) {

        }
    }
}