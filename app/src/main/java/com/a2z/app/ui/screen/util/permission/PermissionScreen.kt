package com.a2z.app.ui.screen.util.permission

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.extension.isPermanentlyDenied
import com.a2z.app.util.VoidCallback
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PermissionScreen() {

    val viewModel: PermissionViewModel = hiltViewModel()

    val permissionsState =
        rememberMultiplePermissionsState(
            permissions = viewModel.getPermissions().map { it.permission })



    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {
                    Text(
                        viewModel.getTitle(),
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Bold, lineHeight = 38.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    BuildPermissionIcons()

                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = MaterialTheme.shapes.medium)
                            .background(
                                color = MaterialTheme.colors.primary.copy(alpha = 0.1f)
                            )
                            .padding(16.dp)
                    ) {
                        viewModel.allPermissionIsPermanentlyDenied.value =
                            permissionsState.permissions.any {
                                it.isPermanentlyDenied(viewModel.appPreference.getBoolean(it.permission))
                            }
                        permissionsState.permissions.forEachIndexed { index, perm ->

                            BuildPermissionComponent(
                                index = index, permissionState = perm
                            )
                        }
                    }

                }

                BuildActionButtons(permissionsState, viewModel)
            }
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ColumnScope.BuildActionButtons(
    permissionsState: MultiplePermissionsState,
    viewModel: PermissionViewModel
) {

    val context = LocalContext.current
    val navController = LocalNavController.current

    @Composable
    fun BuildButton(
        text: String,
        background: Color = MaterialTheme.colors.primary,
        callback: VoidCallback,
    ) {
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            shape = CircleShape,
            onClick = callback,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = background,
                contentColor = Color.White
            )
        ) { Text(text = text) }
    }


    if (permissionsState.allPermissionsGranted) {
        BuildButton(text = "All Done Go Back", background = GreenColor) {
            navController.navigateUp()
        }
    } else if (!viewModel.allPermissionIsPermanentlyDenied.value) {
        BuildButton(text = "Grant Permissions") {
            permissionsState.launchMultiplePermissionRequest()
        }
    } else {
        BuildButton(text = "Go to App Setting", background = RedColor) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun BuildPermissionComponent(
    index: Int, permissionState: PermissionState
) {
    val viewModel: PermissionViewModel = hiltViewModel()

    viewModel.getPermissions()[index].isAccepted = permissionState.hasPermission
    val appPermission = viewModel.getPermissions()[index]

    val isRequestedFirst = viewModel.appPreference.getBoolean(appPermission.permission)
    val isPermissionDeniedPermanently = permissionState.isPermanentlyDenied(isRequestedFirst)

    @Composable
    fun BuildIcon(icon: ImageVector, background: Color) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(28.dp)
                .background(color = background, shape = CircleShape)
                .padding(5.dp)
        )
    }

    @Composable
    fun BuildItem(appPermission: AppPermission) {

        val message = when (appPermission.isAccepted) {
            true -> "Permission is accepted!"
            false -> "Permission is needed to access further feature!"
            null -> "Permission is permanently denied! please grant it from app setting"
        }

        Column(modifier = Modifier.padding(vertical = 5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appPermission.title,
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(text = message)
                }

                when (appPermission.isAccepted) {
                    true -> BuildIcon(
                        icon = Icons.Default.Done,
                        background = GreenColor
                    )
                    false -> BuildIcon(
                        icon = Icons.Default.Info,
                        background = Color.Gray
                    )
                    null -> BuildIcon(
                        icon = Icons.Default.Cancel,
                        background = RedColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
        }
    }



    if (permissionState.shouldShowRationale) {
        viewModel.appPreference.saveBoolean(appPermission.permission, true)
    }

    if (permissionState.hasPermission) {
        appPermission.isAccepted = true
        BuildItem(appPermission)
    } else if (isPermissionDeniedPermanently) {
        appPermission.isAccepted = null
        BuildItem(appPermission)
    } else {
        appPermission.isAccepted = false
        BuildItem(appPermission)
    }


}

@Composable
fun BuildPermissionIcons() {

    val viewModel: PermissionViewModel = hiltViewModel()

    @Composable
    fun BuildIcon(@DrawableRes icon: Int) {
        val primaryColor = MaterialTheme.colors.primary
        val shape = MaterialTheme.shapes.medium
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 24.dp)
                .size(100.dp)
                .clip(shape = shape)
                .background(
                    color = primaryColor.copy(alpha = 0.1f), shape = shape
                )
                .padding(5.dp)
        )
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        content = {
            items(viewModel.getIconList()){
                BuildIcon(icon = it)
            }
        })

}

