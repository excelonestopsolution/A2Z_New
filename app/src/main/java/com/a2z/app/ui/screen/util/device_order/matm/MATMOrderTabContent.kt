package com.a2z.app.ui.screen.util.device_order.matm

import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.data.model.matm.MatmServiceInformation
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.common.AppCheckBox
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.PickCameraAndGalleryImage
import com.a2z.app.ui.component.common.*
import com.a2z.app.ui.component.permission.PermissionComponent
import com.a2z.app.ui.dialog.ConfirmActionDialog
import com.a2z.app.ui.dialog.ImageNetworkDialog
import com.a2z.app.ui.screen.util.device_order.MATMDocumentType
import com.a2z.app.ui.screen.util.device_order.MATMOderViewModel
import com.a2z.app.ui.screen.util.permission.AppPermissionList
import com.a2z.app.ui.screen.util.permission.PermissionType
import com.a2z.app.ui.theme.*
import com.a2z.app.ui.util.extension.dashedBorder
import com.a2z.app.util.BitmapUtil

@Composable
fun MATMOrderTabContent() {

    val viewModel: MATMOderViewModel = hiltViewModel()

    BaseContent(viewModel) {

        ConfirmActionDialog(
            state = viewModel.orderConfirmDialogState,
            title = "Order Confirmation ?",
            description = "M-ATM purchase confirmation. Once purchased than can not be reverse it."
        ) {
            viewModel.onConfirmOrderDevice()
        }

        ImageNetworkDialog(
            url = viewModel.selectedImageUrl.value,
            state = viewModel.showImageDialog
        )

        ObsComponent(flow = viewModel.deviceInfoResultFlow) {
            if (it.status == 2 ||
                it.data?.service_status == "4"
            ) {
                OrderDeviceContent()
            } else if (it.status == 1) {
                DeviceOrderFormContent(it.data)
            }
        }
    }


}

@Composable
fun DeviceOrderFormContent(it: MatmServiceInformation?) {

    fun getStatus(value: String?): Int {
        if (value == null) return 0
        if (value.isEmpty()) return 0
        return 1
    }

    val viewModel: MATMOderViewModel = hiltViewModel()
    val input = viewModel.input

    val panImageStatus = (it?.pangImageStatus ?: "0").toInt()
    val addressProofImageStatus = (it?.addressProofImageStatus ?: "0").toInt()
    var shopAddressStatus = getStatus(it?.shopAddress)
    val shopNameStatus = getStatus(it?.shopName)
    val mobileStatus = getStatus(it?.mobile)
    val emailStatus = getStatus(it?.email)
    val nameStatus = getStatus(it?.name)
    val cityStatus = getStatus(it?.city)
    val aadhaarStatus = getStatus(it?.aadhaarNumber)
    val panStatus = getStatus(it?.panNumber)
    val courierAddressStatus = getStatus(it?.courierAddress)
    val landmarkStatus = getStatus(it?.landmark)
    val pinCodeStatus = getStatus(it?.pinCode)
    val gstStatus = getStatus(it?.gstNumber)
    if (courierAddressStatus == 0) shopAddressStatus = 0

    var hideProceedButton = false
    val hideGstAndEditButton = (
            landmarkStatus == 1
                    && shopAddressStatus == 1
                    && courierAddressStatus == 1
                    && pinCodeStatus == 1
                    && shopNameStatus == 1
                    && mobileStatus == 1
                    && emailStatus == 1
                    && nameStatus == 1
                    && aadhaarStatus == 1
                    && panStatus == 1
                    && cityStatus == 1)

    if (hideGstAndEditButton)
        hideProceedButton = ((panImageStatus == 3 || panImageStatus == 1)
                && (addressProofImageStatus == 3 || addressProofImageStatus == 1))

    input.name.setValidation(nameStatus != 1)
    input.mobile.setValidation(mobileStatus != 1)
    input.email.setValidation(emailStatus != 1)
    input.shopName.setValidation(shopNameStatus != 1)
    input.shopAddress.setValidation(shopAddressStatus != 1)
    input.landmark.setValidation(landmarkStatus != 1)
    input.city.setValidation(cityStatus != 1)
    input.pinCode.setValidation(pinCodeStatus != 1)
    input.panNumber.setValidation(panStatus != 1)
    input.aadhaarNumber.setValidation(aadhaarStatus != 1)
    input.courierAddress.setValidation(courierAddressStatus != 1)
    input.gstNumber.setValidation(!hideGstAndEditButton)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())

        ) {

            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                BuildItem(
                    docType = MATMDocumentType.PAN_CARD,
                    status = panImageStatus,
                    image = it?.panImage
                )
                Spacer(modifier = Modifier.width(8.dp))
                BuildItem(
                    docType = MATMDocumentType.ADDRESS_PROOF,
                    status = addressProofImageStatus,
                    image = it?.addressProofImage
                )
            }

            if (
                panImageStatus == 4
                || panImageStatus == 1
                || addressProofImageStatus == 4
                || addressProofImageStatus == 1
            ) {
                if (!it?.remark.isNullOrEmpty()) Text(
                    it!!.remark.toString(),
                    color = PrimaryColorDark,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )
            }





            if (!hideGstAndEditButton) Button(onClick = {
                viewModel.onEditButton()
            }, modifier = Modifier.align(Alignment.End)) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Edit details")
            }

            if (nameStatus != 1)
                AppTextField(
                    value = input.name.getValue(),
                    label = "Name*",
                    onChange = { input.name.onChange(it) },
                    error = input.name.formError(),
                    readOnly = input.name.readOnly.value
                )
            if (mobileStatus != 1) MobileTextField(
                value = input.mobile.getValue(),
                label = "Mobile Number*",
                onChange = { input.mobile.onChange(it) },
                error = input.mobile.formError(),
                readOnly = input.mobile.readOnly.value
            )
            if (emailStatus != 1) EmailTextField(
                value = input.email.getValue(),
                label = "Email Id*",
                onChange = { input.email.onChange(it) },
                error = input.email.formError(),
                readOnly = input.email.readOnly.value

            )
            if (shopNameStatus != 1) AppTextField(
                value = input.shopName.getValue(),
                label = "Shop Name*",
                onChange = { input.shopName.onChange(it) },
                error = input.shopName.formError(),
                readOnly = input.shopName.readOnly.value
            )

            if (shopAddressStatus != 1) AppTextField(
                value = input.shopAddress.getValue(),
                label = "Shop Address*",
                onChange = { input.shopAddress.onChange(it) },
                error = input.shopAddress.formError(),
                readOnly = input.shopAddress.readOnly.value
            )
            if (landmarkStatus != 1)
                AppTextField(
                    value = input.landmark.getValue(),
                    label = "Landmark*",
                    onChange = { input.landmark.onChange(it) },
                    error = input.landmark.formError()
                )

            if (cityStatus != 1) AppTextField(
                value = input.city.getValue(),
                label = "City*",
                onChange = { input.city.onChange(it) },
                error = input.city.formError()
            )


            if (pinCodeStatus != 1) AppTextField(
                value = input.pinCode.getValue(),
                label = "Pin Code*",
                maxLength = 6,
                keyboardType = KeyboardType.Number,
                onChange = { input.pinCode.onChange(it) },
                error = input.pinCode.formError(),
                readOnly = input.pinCode.readOnly.value
            )


            if (panStatus != 1) AppTextField(
                value = input.panNumber.getValue(),
                label = "Pan Number*",
                maxLength = 10,
                keyboardType = KeyboardType.Text,
                keyboardCapitalization = KeyboardCapitalization.Characters,
                onChange = { input.panNumber.onChange(it) },
                error = input.panNumber.formError()
            )

            if (aadhaarStatus != 1) AadhaarTextField(
                value = input.aadhaarNumber.getValue(),
                onChange = { input.aadhaarNumber.onChange(it) },
                label = "Aadhaar Number*",
                error = input.aadhaarNumber.formError(),
                readOnly = input.aadhaarNumber.readOnly.value
            )
            if (courierAddressStatus != 1) AppTextField(
                value = input.courierAddress.getValue(),
                label = "Courier Address*",
                hint = if (viewModel.courierAddressCheckBoxState.value) "Don't need to fill" else "Required*",
                onChange = { input.courierAddress.onChange(it) },
                error = input.courierAddress.formError(),
                readOnly = viewModel.courierAddressCheckBoxState.value
            )

            if (courierAddressStatus != 1) AppCheckBox(
                title = "Same as shop address",
                value = viewModel.courierAddressCheckBoxState.value,
                onChange = {
                    viewModel.courierAddressCheckBoxState.value = it
                    viewModel.courierAddressValidation.value = !it
                    viewModel.input.courierAddress.setValue("")
                    viewModel.input.courierAddress.clearFormError()
                })

            if (!hideGstAndEditButton)
                if (gstStatus != 1) AppTextField(
                    value = input.gstNumber.getValue(),
                    label = "GST Number",
                    hint = "(Optional)",
                    onChange = { input.gstNumber.onChange(it) },
                    error = input.gstNumber.formError()
                )

            VerifyMatmReceivedContent(
                panImageStatus,
                addressProofImageStatus,
                it
            )

            MatmOtpVerifyContent(panImageStatus, addressProofImageStatus, it?.otpVerify)

            if (!hideProceedButton || viewModel.isMatmReceivedState.value) Button(
                enabled = input.isValidObs.value,
                onClick = { viewModel.onFormSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(52.dp)

            ) {
                Text(text = "Submit")
            }


        }
    }
}

@Composable
fun VerifyMatmReceivedContent(
    panImageStatus: Int,
    addressProofImageStatus: Int,
    it: MatmServiceInformation?,
) {

    val viewModel: MATMOderViewModel = hiltViewModel()

    if (
        panImageStatus == 1 &&
        addressProofImageStatus == 1 &&
        it?.service_status == "1"
    ) {

        if (it.matmServiceStatus == "0") {
            if (it.isMatmReceived == "0") {

                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Note : Please click on below checkbox and submit when you will received micro atm device successfully",
                        fontSize = 12.sp,
                    )

                    AppCheckBox(
                        title = "Is M-Atm received ? ",
                        value = viewModel.isMatmReceivedState.value, onChange = {
                            viewModel.isMatmReceivedState.value = it
                        })
                }


            } else {
                Text(
                    text = "Applied successfully. Your request in pending, please wait for approval.",
                    color = YellowColor,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        } else if (it.matmServiceStatus == "1") {
            Text(
                text = "Application for purchase matm approved. Please do transactions. Thank you",
                color = GreenColor,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun MatmOtpVerifyContent(
    panImageStatus: Int,
    addressProofImageStatus: Int,
    otpVerify: String?,

    ) {
    val viewModel: MATMOderViewModel = hiltViewModel()

    if (panImageStatus == 3
        && addressProofImageStatus == 3
        && otpVerify == "0"
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(color = RedColor, width = 1.dp)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!viewModel.otpSentState.value) TextButton(onClick = { viewModel.requestVerifyOrderOtp() }) {
                Text(text = "Click to verify device order")
            }
            else {

                PinTextField(
                    value = viewModel.otpInput.otp.getValue(),
                    onChange = { viewModel.otpInput.otp.onChange(it) },
                    error = viewModel.otpInput.otp.formError()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    enabled = viewModel.otpInput.isValidObs.value,
                    onClick = {
                        viewModel.onVerifyOtp()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(text = "Verify Otp")
                }
            }

        }
    }

}

@Composable
private fun OrderDeviceContent() {

    val viewModel: MATMOderViewModel = hiltViewModel()

    Card(modifier = Modifier.padding(12.dp), shape = MaterialTheme.shapes.medium) {

        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {

            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,
            ) {

                Image(
                    painter = painterResource(id = R.drawable.matm_order),
                    contentDescription = null,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(shape = MaterialTheme.shapes.medium)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Micro -ATM Service Charge",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColorDark,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Rs. 1999/- + GST",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RedColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Note 18% GST will be applicable", color = PrimaryColorDark.copy(0.7f)
                )

                Text(
                    text = "Convert your shop into miniATM with micro ATM:-",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "* Start your own ATM facility at the shop with at the best micro ATM price.\\n\n" + "* Provide withdrawal services from debits cards of any Bank across India\\n\n" + "*Earn the best income on mATM services\n" + "*Easy to carry and set-up in small shops",
                    color = Color.DarkGray.copy(0.8f),
                    modifier = Modifier.padding(top = 5.dp)

                )

                Text(
                    text = "Benefits & Services offered to our Agents:-",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "* Cash Withdrawal\n" + "* Balance Enquiry\n" + "* Best mini ATM machine price\n" + "*Portable mATM device that is easy to use\n" + "* No waiting in long queues at banks or ATMs",
                    color = Color.DarkGray.copy(0.8f),
                    modifier = Modifier.padding(top = 5.dp, bottom = 16.dp)
                )
            }


            Button(
                onClick = {
                    viewModel.orderConfirmDialogState.value = true
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(text = "Place Order")
            }

        }

    }
}


@Composable
private fun RowScope.BuildItem(
    docType: MATMDocumentType,
    status: Int,
    image: String?
) {

    val viewModel: MATMOderViewModel = hiltViewModel()

    val title = if (docType == MATMDocumentType.PAN_CARD) "Pan Card" else "Address Proof"


    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
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
private fun BuildUpload(title: String, docType: MATMDocumentType) {

    val viewModel: MATMOderViewModel = hiltViewModel()
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

                viewModel.onPickFile(docType, it)
                imageUri.value = it?.toUri()
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
            fontWeight = FontWeight.W500
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
            fontWeight = FontWeight.W500
        )


    }

}
