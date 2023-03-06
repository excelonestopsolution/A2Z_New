package com.a2z.app.ui.screen.aeps.diaogs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.R
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.util.VoidCallback


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AepsShopSetupDialog(
    state: MutableState<Boolean>,
    onDismiss: VoidCallback,
) {


    if (state.value)
        Dialog(
            onDismissRequest = { onDismiss.invoke() },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {

            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )

                    Text(
                        text = "Setup Your Shop Location", style = MaterialTheme.typography.h6.copy(
                            color = RedColor,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Column(
                        modifier = Modifier
                            .background(
                                Color.Gray.copy(0.1f),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text ="AEPS ट्रांसक्शन के लिए दुकान का लोकेशन सेट करना अनिवार्य है।  सुनिचित की गई जगह पर ही आप ट्रांसक्शन कर सके है।  साहहए के लिए आप हमारे कस्टमर एक्सक्यूटिव से संपर्क कर सकते है।  आपका धन्यवाद।",
                            lineHeight = 22.sp
                        )
                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        Text(
                            text = "It is mandatory to set the location of the shop for AEPS transaction. You can do the transaction only at the designated place. For assistance you can contact our customer executive. thank you",
                            lineHeight = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape =
                        RoundedCornerShape(0.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Set Shop Location")
                    }

                }

            }

        }


}