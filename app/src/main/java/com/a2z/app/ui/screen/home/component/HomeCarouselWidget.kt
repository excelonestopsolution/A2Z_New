package com.a2z.app.ui.screen.home.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.a2z.app.ui.screen.home.HomeViewModel
import com.a2z.app.ui.theme.BackgroundColor2
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeCarouselWidget(
    viewModel: HomeViewModel = hiltViewModel()
) {


    if (viewModel.isSliderVisible.value) Column {
        val pagerState = rememberPagerState(pageCount = viewModel.sliders.count())
        LaunchedEffect(key1 = pagerState.currentPage) {
            launch {
                delay(3000)
                with(pagerState) {
                    val target = if (currentPage < pageCount - 1) currentPage + 1 else 0
                    animateScrollToPage(
                        page = target,
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }

        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp, top = 8.dp)
        ) { page ->
            Card(modifier = Modifier
                .height(156.dp)

                .fillMaxWidth()
                .padding(12.dp, 0.dp, 12.dp, 8.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = 16.dp

            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = viewModel.sliders[page].image),
                    contentDescription = "Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                        .background(BackgroundColor2)
                        .padding(5.dp)
                        .clip(MaterialTheme.shapes.small)
                )
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(5.dp)
        )
    }
}