package com.a2z.app.ui.screen.util.indonepal

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
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
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.data.model.indonepal.INActivationInitialResponse
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.AppTextField
import com.a2z.app.ui.component.common.FileTextField
import com.a2z.app.ui.component.permission.CheckCameraStoragePermission
import com.a2z.app.ui.theme.*
import com.a2z.app.util.AppUtil
import com.a2z.app.util.FilePath
import com.a2z.app.util.FileUtil
import java.io.File


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
                        if (it.data.service_status == "1")
                            BuildAlreadyActivated()
                        else BuildMainContent(viewModel, it)
                    }
                } else viewModel.showObsAlertDialog(it.message)
            }

        }
    }
}

@Composable
private fun BuildAlreadyActivated() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.icon_sucess),
                contentDescription = null,
                colorFilter = ColorFilter.tint(GreenColor),
                modifier = Modifier.size(90.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Service is activated!",
                fontWeight = FontWeight.SemiBold,
                color = GreenColor,
                fontSize = 14.sp
            )
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

        fun getFileFromUri(context: Context, uri: Uri?): File? {
            uri ?: return null
            uri.path ?: return null

            var newUriString = uri.toString()
            newUriString = newUriString.replace(
                "content://com.android.providers.downloads.documents/",
                "content://com.android.providers.media.documents/"
            )
            newUriString = newUriString.replace(
                "/msf%3A", "/image%3A"
            )
            val newUri = Uri.parse(newUriString)

            var realPath = String()
            val databaseUri: Uri
            val selection: String?
            val selectionArgs: Array<String>?
            if (newUri.path?.contains("/document/image:") == true) {
                databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                selection = "_id=?"
                selectionArgs = arrayOf(DocumentsContract.getDocumentId(newUri).split(":")[1])
            } else {
                databaseUri = newUri
                selection = null
                selectionArgs = null
            }
            try {
                val column = "_data"
                val projection = arrayOf(column)
                val cursor = context.contentResolver.query(
                    databaseUri,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
                cursor?.let {
                    if (it.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(column)
                        realPath = cursor.getString(columnIndex)
                    }
                    cursor.close()
                }
            } catch (e: Exception) {
                Log.i("GetFileUri Exception:", e.message ?: "")
            }
            val path = realPath.ifEmpty {
                when {
                    newUri.path?.contains("/document/raw:") == true -> newUri.path?.replace(
                        "/document/raw:",
                        ""
                    )
                    newUri.path?.contains("/document/primary:") == true -> newUri.path?.replace(
                        "/document/primary:",
                        "/storage/emulated/0/"
                    )
                    else -> return null
                }
            }
            return if (path.isNullOrEmpty()) null else File(path)
        }


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

    Column(
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

        Text(
            text = "Courier", style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (getStatus(it.data.courier_status) == 0)
            Column() {
                AppTextField(value = "", label = "Courier Name")
                AppTextField(value = "", label = "Docket Number")
                AppTextField(value = "", label = "Courier Dispatch Date")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Submit")
                }
            }
        else if (getStatus(it.data.courier_status) == 1) {
            BuildStatusContent(text = "Courier Approved", isPending = false)
        } else if (getStatus(it.data.courier_status) == 3) {
            BuildStatusContent(text = "Courier Screening...", isPending = true)
        } else if (getStatus(it.data.courier_status) == 4) {
            Text(text = "Courier Reject ! Please content with admin", color = RedColor)
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
        onClick = { viewModel.onActivation() },
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

