package com.a2z.app.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.a2z.app.ui.theme.CircularShape
import com.a2z.app.ui.theme.GreenColor
import com.a2z.app.ui.theme.PrimaryColorDark

@Composable
fun StepperComponent(
    modifier: Modifier = Modifier,
    totalCount: Int,
    selectedIndex: Int = 0,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {


            for (i in 1..totalCount) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = if (i != totalCount) Modifier.weight(1f) else Modifier
                ) {

                    var isSuccess = false
                    var isSelected = false
                    if (i < selectedIndex) isSuccess = true
                    if (i == selectedIndex) isSelected = true


                    var color = Color.Gray
                    if (isSuccess) color = GreenColor
                    if (isSelected) color = PrimaryColorDark

                    Box(
                        modifier = Modifier
                            .clip(CircularShape)
                            .background(color)
                            .size(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSuccess) Image(
                            painter = painterResource(id = com.a2z.app.R.drawable.icon_sucess),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        else Text(
                            text = i.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }


                    if (i != totalCount) Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color)
                            .height(2.dp)
                    )
                }
            }


        }
        Spacer(modifier = Modifier.height(16.dp))
        content.invoke()
    }

}