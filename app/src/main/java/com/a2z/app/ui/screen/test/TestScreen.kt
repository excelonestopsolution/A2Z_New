package com.a2z.app.ui.screen.test

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.common.AppNetworkImage
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.PinTextField
import com.a2z.app.ui.screen.AppViewModel
import com.a2z.app.ui.screen.dmt.beneficiary.register.upi.UpiBeneficiaryRegisterScreen
import com.a2z.app.ui.theme.*
import com.a2z.app.util.AppUtil
import com.a2z.app.util.VoidCallback
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TestScreen() {

    val appViewModel: AppViewModel = hiltViewModel()

    Scaffold(
        backgroundColor = BackgroundColor
    ) {

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {



        }
    }
}


