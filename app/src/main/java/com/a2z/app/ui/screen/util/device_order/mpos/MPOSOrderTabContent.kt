package com.a2z.app.ui.screen.util.device_order.mpos

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.data.model.matm.MatmServiceInformation
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.PickCameraAndGalleryImage
import com.a2z.app.ui.component.common.AppDropDownMenu
import com.a2z.app.ui.component.permission.PermissionComponent
import com.a2z.app.ui.screen.util.device_order.MATMDocumentType
import com.a2z.app.ui.screen.util.device_order.MATMOderViewModel
import com.a2z.app.ui.screen.util.permission.AppPermissionList
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.*
import com.a2z.app.ui.util.extension.dashedBorder
import com.a2z.app.util.BitmapUtil
import com.a2z.app.util.FileUtil


@Composable
fun MPOSOrderTabContent() {
    val viewModel: MPOSOrderViewModel = hiltViewModel()
    BaseContent(viewModel) {

        ObsComponent(flow = viewModel.deviceInfoResultFlow) {
            if (it.status == 1)
                if (it.data?.pangImageStatus == "1" ||
                    it.data?.pangImageStatus == "3"
                ) MPOSFormContent(it.data)
                else MPOSOrderContent()
            else MPOSOrderContent()
        }
    }
}

@Composable
fun MPOSOrderContent() {

    val viewModel: MPOSOrderViewModel = hiltViewModel()

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.matm_order), contentDescription = null,
                modifier = Modifier
                    .size(180.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = "Before MPOS, need to place order the device or activate the service.",
                color = RedColor, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Button(onClick = { viewModel.onRefresh() }) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Refresh")
            }

        }
    }

}

@Composable
private fun MPOSFormContent(data: MatmServiceInformation) {

    val viewModel: MPOSOrderViewModel = hiltViewModel()


    fun getFileStatus(value: String?): Int {
        if (value == null) return 0
        if (value.isEmpty()) return 0
        return value.toInt()
    }

    fun getNonFileStatus(value: String?): Int {
        if (value == null) return 0
        if (value.isEmpty()) return 0
        return 1
    }


    val businessLegalityImageStatus = getFileStatus(data.businessLegalityImageStatus)
    val businessAddressImageStatus = getFileStatus(data.business_address_proof_image_status)
    val shopInsideImageStatus = getFileStatus(data.shop_inside_image_status)
    val shopOutsideImageStatus = getFileStatus(data.shop_outside_image_status)

    var businessLegalityType = data.businessLegalityType ?: ""
    var businessAddressType = data.businessAddressType ?: ""

    if (businessLegalityImageStatus == 4) businessLegalityType = ""
    if (businessAddressImageStatus == 4) businessAddressType = ""

    viewModel.selectedBusinessProofProofTypeState.value = businessAddressType
    viewModel.selectedBusinessLegalityProofTypeState.value = businessLegalityType


    viewModel.businessAddressTypeStatus = getNonFileStatus(businessAddressType)
    viewModel.businessLegalityTypeStatus = getNonFileStatus(businessLegalityType)

    viewModel.businessLegalityTypeStatus = if (businessLegalityType == "") 0 else 1
    viewModel.businessAddressTypeStatus = if (businessAddressType == "") 0 else 1



    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(), shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())) {

            ProofTypeDropDownContent(viewModel)

            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                BuildItem(
                    docType = MPOSDocumentType.SHOP_INSIDE,
                    status = shopInsideImageStatus,
                    image = data.shop_inside_image
                )
                Spacer(modifier = Modifier.width(8.dp))
                BuildItem(
                    docType = MPOSDocumentType.SHOP_OUTSIDE,
                    status = shopOutsideImageStatus,
                    image = data.shop_outside_image
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                BuildItem(
                    docType = MPOSDocumentType.BUSINESS_ADDRESS_PROOF,
                    status = businessAddressImageStatus,
                    image = data.business_address_proof_image
                )
                Spacer(modifier = Modifier.width(8.dp))
                BuildItem(
                    docType = MPOSDocumentType.BUSINESS_LEGALITY_PROOF,
                    status = businessLegalityImageStatus,
                    image = data.business_proof_image
                )
            }

            if (
                !(businessLegalityType.isNotEmpty()
                        && businessAddressType.isNotEmpty() &&
                        data.business_proof_image.toString().isNotEmpty() &&
                        data.business_address_proof_image.toString()
                            .isNotEmpty() &&
                        data.shop_inside_image.toString().isNotEmpty() &&
                        data.shop_outside_image.toString().isNotEmpty() &&
                        data.shop_inside_image_status.toString() != "4" &&
                        data.shop_outside_image_status.toString() != "4" &&
                        data.business_address_proof_image_status.toString() != "4" &&
                        businessLegalityImageStatus.toString() != "4")
            ) {
                Button(
                    onClick = {viewModel.onProceed()}, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(52.dp)

                ) {
                    Text(text = "Proceed")
                }
            }


        }
    }
}

@Composable
private fun ProofTypeDropDownContent(viewModel: MPOSOrderViewModel) {

    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchDocTypeList()
    })

    if (viewModel.docTypeListState.value != null) {


        if (viewModel.businessLegalityTypeStatus == 0) AppDropDownMenu(
            selectedState = viewModel.selectedBusinessLegalityProofTypeState,
            label = "Business Legality Proof Type",
            list = viewModel.docTypeListState.value!!.businessLegalityProofType!!
        )
        if (viewModel.businessAddressTypeStatus == 0) AppDropDownMenu(
            selectedState = viewModel.selectedBusinessProofProofTypeState,
            label = "Business Legality Proof Type",
            list = viewModel.docTypeListState.value!!.businessAddressProofType!!
        )
    }
}


@Composable
private fun RowScope.BuildItem(
    docType: MPOSDocumentType,
    status: Int,
    image: String?
) {

    val viewModel: MATMOderViewModel = hiltViewModel()

    val title = when (docType) {
        MPOSDocumentType.SHOP_INSIDE -> "Shop Inside"
        MPOSDocumentType.SHOP_OUTSIDE -> "Shop Outside"
        MPOSDocumentType.BUSINESS_LEGALITY_PROOF -> "Business Legality Proof"
        MPOSDocumentType.BUSINESS_ADDRESS_PROOF -> "Business Address Proof"
    }


    Box(
        modifier = Modifier
            .weight(1f)

            .dashedBorder(1.dp, color = Color.DarkGray.copy(0.8f), radius = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (status == 3 || status == 1)
            BuildScanning(
                title,
                image,
                status == 3
            )
        else BuildUpload(title, docType)

        Text(
            text = "*", color = RedColor, fontSize = 32.sp,
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.TopEnd)
        )

    }

}


@Composable
private fun BuildUpload(title: String, docType: MPOSDocumentType) {

    val viewModel: MPOSOrderViewModel = hiltViewModel()
    val context = LocalContext.current


    val imageUri = remember {
        mutableStateOf<Uri?>(null)
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)

    ) {


        PickCameraAndGalleryImage(
            onResult = {
                val file = FileUtil.getFile(context, it)
                viewModel.onPickFile(docType, file)
                imageUri.value = it
            },
            content = { capture ->
                PermissionComponent(
                    permissions = AppPermissionList.cameraStorages().map { it.permission }) {

                    if (imageUri.value == null) Icon(
                        imageVector = Icons.Default.CloudUpload, contentDescription = null,
                        Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clickable {
                                val result = it.invoke()
                                if (result) capture.invoke()
                                else {
                                    viewModel.navigateTo(
                                        NavScreen.PermissionScreen.passData(
                                            permissionType = PermissionType.CameraAndStorage
                                        )
                                    )
                                }
                            }, tint = PrimaryColorDark
                    )
                    else {
                        val bitmap = BitmapUtil.uriToBitMap(imageUri.value, context)
                        Image(
                            bitmap = bitmap!!.asImageBitmap(), contentDescription = null,
                            modifier = Modifier
                                .height(180.dp)
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .clickable {
                                    val result = it.invoke()
                                    if (result) capture.invoke()
                                    else {
                                        viewModel.navigateTo(
                                            NavScreen.PermissionScreen.passData(
                                                permissionType = PermissionType.CameraAndStorage
                                            )
                                        )
                                    }
                                },
                            contentScale = ContentScale.FillBounds
                        )
                    }

                }
            }
        )

        Text(
            text = title,
            fontSize = 16.sp, color = PrimaryColorDark,
            modifier = Modifier.padding(5.dp),
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BuildScanning(title: String, image: String?, uploadPending: Boolean) {


    val viewModel: MATMOderViewModel = hiltViewModel()

    val (color, text) = if (uploadPending) Pair(YellowColor, "Scanning...")
    else Pair(GreenColor, "Approved")


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                viewModel.selectedImageUrl.value = image ?: ""
                viewModel.showImageDialog.value = true
            }

    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.height(180.dp)

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
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center
        )


    }

}
