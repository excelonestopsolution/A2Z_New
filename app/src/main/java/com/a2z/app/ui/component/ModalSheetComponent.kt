package com.a2z.app.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalSheetComponent(
    sheetContent : @Composable ()->Unit,
    content : @Composable ( modalBottomSheetState : ModalBottomSheetState,)->Unit
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetContent = {sheetContent()},
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colors.primary,
        // scrimColor = Color.Red,  // Color for the fade background when you open/close the drawer
    ) {
        content(modalBottomSheetState)
    }
}