package com.a2z.app.ui.component


import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.util.VoidCallback



@Composable
fun NavTopBar(
    title: String,
    useDefaultBack: Boolean = true,
    onBackPress: VoidCallback? = null,
) {

    val navController = LocalNavController.current
    TopAppBar(
        title = {
            Text(
                title, style = TextStyle.Default.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            )
        },
        navigationIcon = {
            if (useDefaultBack)
                NavigationIcon(onBackPress = {
                    onBackPress?.invoke() ?: run {
                        navController.navigateUp()
                    }
                })
        }

    )
}

@Composable
private fun NavigationIcon(onBackPress: VoidCallback) {
    IconButton(onClick = onBackPress) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back Arrow"
        )
    }
}