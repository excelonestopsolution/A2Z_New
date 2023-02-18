package com.a2z.app.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.util.PagingState
import com.a2z.app.util.VoidCallback

@Composable
fun <T> AppPagingComponent(
    modifier: Modifier = Modifier,
    pagingState: PagingState<T>,
    onLoadNext: VoidCallback,
    itemContent : @Composable (T)->Unit
) {
    LazyColumn (modifier = modifier){
        items(pagingState.items.size) { index ->

            val it = pagingState.items[index]

            pagingState.shouldLoadNext(index) {
                onLoadNext.invoke()
            }

            itemContent.invoke(it)

        }
        item {
            if (pagingState.items.isEmpty() &&
                !pagingState.isLoading
            ) {
                Box(
                    modifier = Modifier.fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyListComponent()
                }
            }
        }
        item {
            val modifier = if (pagingState.page == 1)
                Modifier.fillParentMaxSize() else Modifier
                .fillMaxWidth()
                .height(100.dp)
            if (pagingState.isLoading && pagingState.exception == null) Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                AppProgress()
            }
        }

        item {
            val modifier = if (pagingState.page == 1)
                Modifier.fillParentMaxSize() else Modifier
                .fillMaxWidth()
                .height(100.dp)
            if (pagingState.exception != null) Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Error, contentDescription = null,
                        tint = RedColor,
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pagingState.exception!!.message.toString(),
                        fontSize = 14.sp, color = RedColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}