package com.a2z.app.ui.screen.util.pan_service

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.component.ObsComponent
import com.a2z.app.ui.component.common.AppFormCard
import com.a2z.app.ui.component.common.AppFormUI
import com.a2z.app.ui.component.common.AppNetworkImage2
import com.a2z.app.ui.dialog.ConfirmActionDialog
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.RedColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PanServiceScreen() {


    val viewModel: PanServiceViewModel = hiltViewModel()
    Scaffold(backgroundColor = BackgroundColor, topBar = { NavTopBar(title = "Pan Service") }) {

        BaseContent(viewModel) {

            ObsComponent(flow = viewModel.serviceNoteStateFlow) {
                AppFormUI(
                    showWalletCard = false,
                    button = {
                        Button(
                            onClick = {
                                viewModel.onActivate()
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(text = "Activate Service")
                        }
                    },
                    cardContents = listOf(
                        AppFormCard(title = "Note : ", contents = {
                            it.note.forEachIndexed { index, item ->

                                Row {
                                    Text(
                                        text = "${index.plus(1)}. ",
                                        fontWeight = FontWeight.Bold,
                                    )

                                    Text(
                                        text = item,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = PrimaryColor.copy(alpha = 0.8f)
                                    )
                                }

                            }
                        }),
                        AppFormCard(title = "Activation Message : ", contents = {


                            it.serviceActivationMessage.forEachIndexed { index, item ->

                                Row {
                                    Text(
                                        text = "${index.plus(1)}. ",
                                        fontWeight = FontWeight.Bold,
                                    )

                                    Text(
                                        text = item,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = RedColor.copy(alpha = 0.8f)
                                    )
                                }

                            }
                        }),

                        AppFormCard {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                AppNetworkImage2(
                                    url = it.image.panImage,
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.small)
                                        .weight(1f)
                                        .height(200.dp),

                                )
                                AppNetworkImage2(
                                    url = it.image.utiImage,
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.small)
                                        .weight(1f)
                                        .height(200.dp)
                                )

                            }
                        },
                    )
                )

                ConfirmActionDialog(
                    state = viewModel.confirmDialogState,
                    title = "Confirm Pan Activation",
                    description = it.serviceActivationMessage.first(),
                ) {
                    viewModel.onConfirmActivation()

                }

            }
        }

    }
}