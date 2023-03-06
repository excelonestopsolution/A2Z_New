package com.a2z.app.ui.screen.kyc.document

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.dialog.ImageDialog
import com.a2z.app.ui.dialog.ImageNetworkDialog
import com.a2z.app.ui.theme.*
import com.a2z.app.ui.util.extension.dashedBorder

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DocumentKycScreen() {

    val viewModel: DocumentKycViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Document Kyc") }
    ) {

        BaseContent(viewModel) {
            Card(
                shape = MaterialTheme.shapes.medium,
                elevation = 8.dp,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize()
            ) {

                ObsComponent(flow = viewModel.documentKycDetailResultFlow) {
                  if(it.status ==1)  Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .verticalScroll(rememberScrollState())
                    ) {


                        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                            BuildItem(DocumentKycType.PAN_CARD_FRONT)
                            Spacer(modifier = Modifier.width(8.dp))
                            BuildItem(DocumentKycType.PROFILE_PHOTO)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                            BuildItem(DocumentKycType.AADHAAR_FRONT)
                            Spacer(modifier = Modifier.width(8.dp))
                            BuildItem(DocumentKycType.AADHAAR_BACK)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                            BuildItem(DocumentKycType.SHOP_IMAGE)
                            Spacer(modifier = Modifier.width(8.dp))
                            BuildItem(DocumentKycType.CANCEL_CHEQUE)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                            BuildItem(DocumentKycType.SEAL_CHEQUE)
                            Spacer(modifier = Modifier.width(8.dp))
                            BuildItem(DocumentKycType.GST_IMAGE)
                        }

                    }
                    else viewModel.showObsAlertDialog(it.message)
                }
            }
        }

        ImageDialog(
            imageDialogOpen = viewModel.previewDialogState,
            selectFile = viewModel.selectedFile
        ) {
            viewModel.uploadDoc()
        }

        ImageNetworkDialog(
            url = viewModel.selectedUrlState.value,
            state = viewModel.showImageState
        )
    }
}


@Composable
private fun RowScope.BuildItem(
    docType: DocumentKycType,
) {

    val viewModel: DocumentKycViewModel = hiltViewModel()
    val (title, image, status) = viewModel.getDocDetail(docType)
    val (upload, uploaded, uploadPending) = when (status) {
        "0" -> Triple(true, false, false)
        "1" -> Triple(false, true, false)
        else -> Triple(false, false, true)
    }

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .dashedBorder(1.dp, color = Color.DarkGray.copy(0.8f), radius = 10.dp),
        contentAlignment = Alignment.Center
    ) {

        if (uploaded || uploadPending) BuildScanning(title, image, uploadPending)
        if (upload) BuildUpload(title, docType)

        Text(
            text = "*", color = RedColor, fontSize = 32.sp,
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.TopEnd)
        )

    }

}

@Composable
private fun BuildUpload(title: String, docType: DocumentKycType) {

    val viewModel: DocumentKycViewModel = hiltViewModel()

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
        Icon(
            imageVector = Icons.Default.CloudUpload, contentDescription = null,
            Modifier.size(90.dp), tint = PrimaryColorDark
        )

        PickCameraAndGalleryImage(
            onResult = { viewModel.onPickFile(docType, it) },
            content = { action ->
                Button(
                    onClick = { action.invoke() },
                    shape = CircularShape
                ) { Text(text = "Upload") }
            }
        )



        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = title,
            fontSize = 16.sp, color = PrimaryColorDark,
            modifier = Modifier.padding(5.dp),
            fontWeight = FontWeight.W500
        )
    }
}


@Composable
private fun BuildScanning(title: String, image: String, uploadPending: Boolean) {

    val viewModel: DocumentKycViewModel = hiltViewModel()

    val (color, text) = if (uploadPending) Pair(YellowColor, "Scanning...")
    else Pair(GreenColor, "Approved")


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                viewModel.selectedUrlState.value = image
                viewModel.showImageState.value = true
            }

    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)

        ) {
            Image(
                painter = painterResource(id = com.a2z.app.R.drawable.icon_sucess),
                contentDescription = "Done",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(color)
            )

            Text(
                text = "File Uploaded",
                fontSize = 12.sp, color = PrimaryColor.copy(0.8f),
                modifier = Modifier.padding(
                    top = 5.dp,
                    bottom = 2.dp
                )
            )

            Text(
                text = text,
                fontSize = 14.sp, color = color,
                modifier = Modifier.padding(2.dp),
                fontWeight = FontWeight.W500
            )

            Text(
                text = "(Tap to view Image)",
                fontSize = 12.sp, color = PrimaryColorDark.copy(0.7f),
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.W400
            )
        }

        Text(
            text = title,
            fontSize = 16.sp, color = PrimaryColorDark,
            modifier = Modifier.padding(5.dp),
            fontWeight = FontWeight.W500
        )


    }

}