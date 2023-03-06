package com.a2z.app.ui.screen.aeps.diaogs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
fun AepsShopWarningDialog(
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
                    .border(5.dp, RedColor)
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

                    Box(
                        modifier = Modifier
                            .height(140.dp)
                            .width(120.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.shop),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )

                        Image(
                            painter = painterResource(id = R.drawable.location_alert),
                            contentDescription = null,
                            modifier = Modifier
                                .size(72.dp)
                                .align(Alignment.BottomEnd)
                        )
                    }

                    Text(
                        text = "Shop Location Alert !", style = MaterialTheme.typography.h6.copy(
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
                            text = "AEPS ट्रांसक्शन की सुविधा केवल आपके शॉप पर है, कृपया आप अपने शॉप पर जाइये। सहायता के लिए आप हमारे कस्टमर केयर कॉल करे।  आपका धन्यवाद। ",
                            lineHeight = 22.sp
                        )
                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        Text(
                            text = "Transaction facility is available only at your shop, please go to your shop. Call our customer care for assistance. thank you",
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
                        Text("Go To Dashboard")
                    }

                }

            }

        }


}