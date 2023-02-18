package com.a2z.app.ui.component


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.util.VoidCallback



@Composable
fun NavTopBar(
    title: String,
    subTitle: String?= null,
    useDefaultBack: Boolean = true,
    onBackPress: VoidCallback? = null,
    actions : (@Composable ()->Unit)? = null
) {

    val navController = LocalNavController.current
    TopAppBar(
        title = {
            Column {

                Text(
                    title, style = TextStyle.Default.copy(fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold)
                )
                if(subTitle !=null) Text(
                    subTitle, style = TextStyle.Default.copy(fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                )
            }
        },
        navigationIcon = {
            if (useDefaultBack)
                NavigationIcon(onBackPress = {
                    onBackPress?.invoke() ?: run {
                        navController.navigateUp()
                    }
                })
        },
        actions = {actions?.invoke()}

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