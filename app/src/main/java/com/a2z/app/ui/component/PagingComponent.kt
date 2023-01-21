package com.a2z.app.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.rememberStateOf

@Composable
fun <T> AppPagingLazyList(pagingData: LazyPagingItems<*>, content: @Composable (T) -> Unit) {

    LazyColumn {
        if (pagingData.loadState.refresh == LoadState.Loading) {
            item {
                Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                    AppProgress()
                }
            }
        }
        if (pagingData.loadState.refresh is LoadState.NotLoading) {

            if (pagingData.itemCount == 0)
                item {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        EmptyListComponent()
                    }
                }
            else items(pagingData) {
                val mData = it as T
                content.invoke(mData)
            }

        }
        if (pagingData.loadState.refresh is LoadState.Error) {
            item { AppLazyPagingListErrorComponent(pagingData) }
        }


        if (pagingData.loadState.append == LoadState.Loading) {
            item {
                AppProgress()
            }
        }
        if (pagingData.loadState.append is LoadState.Error) {
            item { AppLazyPagingListErrorComponent(pagingData) }
        }

        if (pagingData.loadState.prepend == LoadState.Loading) {
            item { AppProgress() }
        }
        if (pagingData.loadState.prepend is LoadState.Error) {
            item { AppLazyPagingListErrorComponent(pagingData) }
        }
    }

}

@Composable
private fun <T : LazyPagingItems<*>> AppLazyPagingListErrorComponent(pagingData: T) {


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Card(
            modifier = Modifier.padding(
                top = 5.dp
            ), shape = RectangleShape,
            backgroundColor = RedColor
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Error occurred!", color = Color.White)
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = { pagingData.retry() }, colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White,
                    )
                ) {
                    Text(text = "Retry")
                }
            }
        }
    }

}