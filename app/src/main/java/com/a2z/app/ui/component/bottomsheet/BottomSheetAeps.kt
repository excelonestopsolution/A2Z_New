package com.a2z.app.ui.component.bottomsheet

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.aeps.RDService
import com.a2z.app.ui.screen.AppViewModel
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.util.extension.showToast

private val rdServiceList = listOf(
    RDService(deviceName = "Morpho", "com.scl.rdservice"),
    RDService(deviceName = "Mantra", "com.mantra.rdservice"),
    RDService(deviceName = "StartTek", "com.acpl.registersdk"),
)

@Composable
fun BottomSheetAepsDevice(onSelect : (RDService)->Unit) {

    val context  = LocalContext.current

    Box(
        Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment =
            Alignment.Start
        ) {
            Text(text = "Select RD Service", style = MaterialTheme.typography.h6.copy(
                color = Color.Black
            ))
            Spacer(modifier = Modifier.height(32.dp))

            rdServiceList.forEachIndexed { index, it ->
                TextButton(onClick = {
                    try {
                        onSelect.invoke(it)
                    }catch (e : Exception){
                        context.showToast("1 "+e.message.toString())
                    }
                }, ) {
                    Text(
                        text = (index +1).toString() +".  "+it.deviceName,
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary),
                        modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth()
                    )
                }
                Divider()
            }

        }
    }
}