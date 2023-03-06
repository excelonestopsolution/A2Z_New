package com.a2z.app.ui.screen.util.indonepal

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.data.model.indonepal.INActivationInitialResponse
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.DateTextField
import com.a2z.app.ui.component.common.FileTextField
import com.a2z.app.ui.component.permission.CheckCameraStoragePermission
import com.a2z.app.ui.dialog.ConfirmActionDialog
import com.a2z.app.ui.theme.*
import com.a2z.app.util.FileUtil
import com.a2z.app.util.extension.removeDateSeparator


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun INServiceActivationScreen() {
    val viewModel: INServiceActivationViewModel = hiltViewModel()
    Scaffold(backgroundColor = BackgroundColor,
        topBar = { NavTopBar(title = "Indo Nepal Activation") }) {

        BaseContent(viewModel) {


            ObsComponent(flow = viewModel.initialDataResultResponse) {
                if (it.status == 1) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        BuildMainContent(viewModel, it)
                    }
                } else viewModel.showObsAlertDialog(it.message)
            }

        }
    }
}

@Composable
private fun BuildMainContent(
    viewModel: INServiceActivationViewModel, it: INActivationInitialResponse
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {


        BuildActivationFormContent(it)


        Spacer(modifier = Modifier.height(8.dp))

        BuildNoteTitle()

        Spacer(modifier = Modifier.height(8.dp))

        BuildNoteAndFormDownload()

        BuildActivationButton(it, viewModel)
    }

    ConfirmActionDialog(
        state = viewModel.confirmDialogState,
        title = "You are sure ?",
        description = viewModel.staticData.warning.first()
    ) {viewModel.onActivation()}
}

@Composable
fun BuildActivationFormContent(it: INActivationInitialResponse) {
    val viewModel: INServiceActivationViewModel = hiltViewModel()

    fun getStatus(value: String?): Int {
        if (value == null || value.trim().isEmpty()) return 0
        return value.toDouble().toInt()
    }

    @Composable
    fun BuildUploadContent() {

        val context = LocalContext.current

        val pdfLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { fileUri ->
            if (fileUri != null) {
                viewModel.selectedFile.value = FileUtil.createTempFileFromUir(context, fileUri)
            }
        }

        val fileName =
            if (viewModel.selectedFile.value == null) "" else viewModel.selectedFile.value!!.name
        CheckCameraStoragePermission {

            FileTextField(value = fileName, label = "Upload File", hint = "Activation pdf file") {
                val result = it.invoke()
                if (result) pdfLauncher.launch("application/pdf")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { viewModel.uploadDoc() }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Upload File")
        }
    }

    @Composable
    fun BuildStatusContent(text: String, isPending: Boolean) {
        val image =
            if (isPending) R.drawable.ic_baseline_image_search_24
            else R.drawable.icon_sucess
        val color = if (isPending) YellowColor else GreenColor
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(color.copy(0.3f), shape = MaterialTheme.shapes.small)
                .padding(5.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = color)
        }
    }

    if (it.data.service_status != "0") Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                1.dp, color = Color.LightGray, shape = MaterialTheme.shapes.small
            )
            .padding(12.dp)
    ) {

        Text(
            text = "Document", style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (getStatus(it.data.document_status) == 4)
            Text(
                text = "Document verification failed! please download and re-upload docs as pdf file",
                style = MaterialTheme.typography.body2,
            )
        if (getStatus(it.data.document_status) == 0 ||
            getStatus(it.data.document_status) == 4
        ) BuildUploadContent()
        else {
            val isDocumentPending = getStatus(it.data.document_status) == 3
            val documentText =
                if (isDocumentPending) "Screening Document.." else "Document Verified!"
            BuildStatusContent(text = documentText, isPending = isDocumentPending)
        }

        if(getStatus(it.data.document_status) == 1){
            Text(
                text = "Courier", style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            if (getStatus(it.data.courier_status) == 0)
                BuildInputForm()
            else if (getStatus(it.data.courier_status) == 1) {
                BuildStatusContent(text = "Courier Approved", isPending = false)
            } else if (getStatus(it.data.courier_status) == 3) {
                BuildStatusContent(text = "Courier Screening...", isPending = true)
            } else if (getStatus(it.data.courier_status) == 4) {
                Text(text = "Courier Reject ! Please content with admin", color = RedColor)
            }
        }

    }


}

@Composable
private fun BuildInputForm() {
    val viewModel : INServiceActivationViewModel = hiltViewModel()
    val input = viewModel.input
    Column {
        AppTextField(
            value = input.courierName.formValue(),
            label = "Courier Name",
            error = input.courierName.formError(),
            onChange = {input.courierName.onChange(it)}
        )
        AppTextField(
            value = input.docketNumber.formValue(),
            label = "Docket Number",
            error = input.docketNumber.formError(),
            onChange = {input.docketNumber.onChange(it)}
        )
         DateTextField(
            value = input.courierDate.formValue(),
            label = "Courier Dispatch Date",
            onChange = { value -> input.courierDate.onChange(value) },
            error = input.courierDate.formError(),
            topSpace = MaterialTheme.spacing.medium,
            downText = "hint : dob form 01/01/1970",
            onDateSelected = { input.courierDate.onChange(it.removeDateSeparator()) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            enabled = input.isValidObs.value,
            onClick = {viewModel.onCourierDataSubmit()},
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Submit")
        }
    }
}


@Composable
private fun BuildNoteTitle() {
    Text(
        text = "Note : ", style = MaterialTheme.typography.h6.copy(
            color = PrimaryColor
        )
    )
}

@Composable
private fun BuildNoteAndFormDownload() {

    val viewModel: INServiceActivationViewModel = hiltViewModel()

    @Composable
    fun BuildButton(
        type: INServiceActivationViewModel.DownloadFormType,
        callback: (INServiceActivationViewModel.DownloadFormType) -> Unit
    ) {

        val (text, color) = when (type) {
            INServiceActivationViewModel.DownloadFormType.SAMPLE -> Pair(
                "Download Sample", YellowColor
            )
            INServiceActivationViewModel.DownloadFormType.FORM -> Pair(
                "Download Form", PrimaryColorLight
            )
        }

        TextButton(
            onClick = { callback.invoke(type) }, colors = ButtonDefaults.textButtonColors(
                contentColor = color
            )
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text)
            }
        }

    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                1.dp, color = Color.LightGray, shape = MaterialTheme.shapes.small
            )
            .padding(8.dp)
    ) {
        BuildButton(INServiceActivationViewModel.DownloadFormType.FORM) {
            viewModel.onDownloadForm(it)
        }

        BuildButton(INServiceActivationViewModel.DownloadFormType.SAMPLE) {
            viewModel.onDownloadForm(it)
        }

        BuildNoteList()
    }
}

@Composable
private fun BuildActivationButton(
    it: INActivationInitialResponse, viewModel: INServiceActivationViewModel
) {
    if (it.data.service_status == "0") Button(
        onClick = {
            viewModel.confirmDialogState.value = true
        },
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .height(52.dp)
    ) {

        Text(text = "Activate Indo Nepal")
    }
}

@Composable
private fun BuildNoteList() {
    val viewModel: INServiceActivationViewModel = hiltViewModel()
    viewModel.staticData.note.forEachIndexed { index, value ->
        val color = viewModel.randomColor(index).copy(alpha = 0.7f)
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(text = (index + 1).toString(), color = color)
            Text(text = " .  ")
            Text(
                text = value,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }


    }
}

