package com.a2z.app.ui.component.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.a2z.app.R
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.util.VoidCallback

@Composable
fun AppNetworkImage(
    url: String,
    placeholderRes: Int = R.drawable.baseline_image_24,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RoundedCornerShape(5.dp),
    size: Int = 52,
    border: Boolean = false,
    onClick : VoidCallback ? = null
) {

    val mPainter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = url)
            .apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                    .transformations(
                    )
                    .build()
            }).build()
    )

    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = mPainter,
            contentDescription = null,
            modifier = Modifier
                .size(size.dp)
                .clip(shape)
                .clickable {
                    onClick?.invoke()
                }
        )


        val state = mPainter.state
        if (state is AsyncImagePainter.State.Loading ||
            state is AsyncImagePainter.State.Error
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(size.dp)
                    .clip(shape = shape)

                    .background(color = Color.Blue.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center

            ) {
                Image(
                    painter = painterResource(id = placeholderRes),
                    contentDescription = null,
                    contentScale = contentScale,
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }
        } else {
            Image(
                painter = mPainter,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(size.dp)
                    .clip(shape)
                    .border(width = 1.dp, shape = CircleShape, color = Color.White),
                contentScale = contentScale,
            )
        }
    }


}


@Composable
fun AppNetworkImage2(
    url: String,
    modifier: Modifier = Modifier,
    placeholderRes: Int = R.drawable.baseline_image_24,
    contentScale : ContentScale = ContentScale.Fit
) {

    val mPainter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = url)
            .apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                    .transformations(
                    )
                    .build()
            }).build()
    )


   Box(contentAlignment = Alignment.Center){
       Image(
           painter = mPainter,
           contentDescription = null,
           modifier = modifier
       )


       val state = mPainter.state
       if (state is AsyncImagePainter.State.Loading ||
           state is AsyncImagePainter.State.Error
       ) {
           Image(
               painter = painterResource(id = placeholderRes),
               contentDescription = null,
               contentScale = contentScale,
               colorFilter = ColorFilter.tint(Color.White),
               modifier = modifier
           )
       } else {
           Image(
               painter = mPainter,
               contentDescription = null,
               contentScale = contentScale,
               modifier = modifier
           )
       }
   }


}