package com.a2z.app.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.a2z.app.ui.theme.*
import com.a2z.app.ui.util.rememberStateOf
import com.a2z.app.util.AppConstant

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MpinInputComponent(
    visibleState: MutableState<Boolean>,
    amount: String? = null,
    onSubmit: (String) -> Unit
) {



    if (visibleState.value) Dialog(
        onDismissRequest = { visibleState.value = false },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        val dotOneState = rememberStateOf(value = "")
        val dotTwoState = rememberStateOf(value = "")
        val dotThreeState = rememberStateOf(value = "")
        val dotFourState = rememberStateOf(value = "")


        fun dotOneIsFilled(): Boolean {
            return dotOneState.value.isNotEmpty()
                    && dotTwoState.value.isEmpty()

        }

        fun dotTwoIsFilled(): Boolean {
            return dotTwoState.value.isNotEmpty()
                    && dotThreeState.value.isEmpty()

        }

        fun dotThreeIsFilled(): Boolean {
            return dotThreeState.value.isNotEmpty()
                    && dotFourState.value.isEmpty()

        }

        fun onTextButtonClick(it: String) {
            if (dotOneState.value.isEmpty())
                dotOneState.value = it
            else if (dotOneIsFilled())
                dotTwoState.value = it
            else if (dotTwoIsFilled())
                dotThreeState.value = it
            else if (dotThreeIsFilled())
                dotFourState.value = it
        }

        fun onBackSpaceClick() {
            if (dotFourState.value.isNotEmpty()) {
                dotFourState.value = ""
            } else if (dotThreeIsFilled()) {
                dotThreeState.value = ""
            } else if (dotTwoIsFilled()) {
                dotTwoState.value = ""
            } else if (dotOneIsFilled()) {
                dotOneState.value = ""
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(
                    shape = MaterialTheme.shapes.small, elevation = 16.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = com.a2z.app.R.drawable.app_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .width(90.dp)
                                .height(70.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        if (amount != null && amount.isNotEmpty()) Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "Transaction Amount",
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = (AppConstant.RUPEE_SYMBOL + " $amount"),
                                fontWeight = FontWeight.Bold,
                                color = PrimaryColorDark,
                                fontSize = 25.sp
                            )
                        }
                    }
                }



                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BuildDot(dotOneState.value.isNotEmpty())
                        BuildDot(dotTwoState.value.isNotEmpty())
                        BuildDot(dotThreeState.value.isNotEmpty())
                        BuildDot(dotFourState.value.isNotEmpty())
                    }


                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Enter Transaction M-PIN",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,

                        )
                }


            }




            Column(
                Modifier
                    .background(BackgroundColor)
                    .padding(16.dp)
            ) {

                Row() {
                    BuildButton(text = "1") { onTextButtonClick(it) }
                    BuildButton(text = "2") { onTextButtonClick(it) }
                    BuildButton(text = "3") { onTextButtonClick(it) }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row() {
                    BuildButton(text = "4") { onTextButtonClick(it) }
                    BuildButton(text = "5") { onTextButtonClick(it) }
                    BuildButton(text = "6") { onTextButtonClick(it) }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row() {
                    BuildButton(text = "7") { onTextButtonClick(it) }
                    BuildButton(text = "8") { onTextButtonClick(it) }
                    BuildButton(text = "9") { onTextButtonClick(it) }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)
                ) {
                    BuildButton(
                        icon = {
                            Icon(imageVector = Icons.Default.Backspace,
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(8.dp)
                                    .clickable {
                                        onBackSpaceClick()
                                    }
                            )
                        }
                    ) {

                    }
                    BuildButton(text = "0") { onTextButtonClick(it) }
                    BuildButton(
                        icon = {

                            val backgroundColor = if (dotFourState.value.isNotEmpty())
                                PrimaryColorDark else Color.Gray
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(CircularShape)
                                        .background(backgroundColor, shape = CircularShape)
                                        .size(52.dp)
                                        .padding(4.dp)
                                        .clickable {
                                            if (dotFourState.value.isNotEmpty()) {
                                                visibleState.value = false
                                                onSubmit.invoke(
                                                    dotOneState.value +
                                                            dotTwoState.value +
                                                            dotThreeState.value +
                                                            dotFourState.value
                                                )
                                            }
                                        },
                                    tint = Color.White
                                )
                            }
                        }
                    ) {

                    }
                }

            }

        }

    }
}


@Composable
fun BuildDot(isVisible: Boolean) {

    val (color, text) = when (isVisible) {
        true -> Pair(PrimaryColorDark, "*")
        false -> Pair(Color.Gray.copy(alpha = 0.7f), "")
    }

    Box(
        modifier = Modifier
            .clip(CircularShape)
            .background(color, CircularShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, fontSize = 32.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .size(42.dp)
                .padding(4.dp)

        )
    }
}

@Composable
private fun RowScope.BuildButton(
    text: String? = null,
    icon: (@Composable () -> Unit)? = null,
    onClick: (String) -> Unit
) {


    if (text != null) Text(
        text = text,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        color = PrimaryColor,
        modifier = Modifier
            .weight(1f)
            .padding(4.dp)
            .clickable { onClick.invoke(text) }
    )
    icon?.invoke()
}