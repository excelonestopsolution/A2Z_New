package com.a2z.app.ui.component.bottomsheet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BackPressHandler
import com.a2z.app.ui.component.keyboardAsState
import com.a2z.app.ui.screen.AppViewModel
import com.a2z.app.util.AppUtil
import com.a2z.app.util.ToggleBottomSheet
import com.a2z.app.util.VoidCallback
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetComponent(
    sheetContent: @Composable (close: VoidCallback) -> Unit,
    mainContent: @Composable (toggle: ToggleBottomSheet) -> Unit,
) {

    rememberBottomSheetScaffoldState(
        bottomSheetState =
        BottomSheetState(BottomSheetValue.Collapsed),
    )
    val coroutineScope = rememberCoroutineScope()


    val manager = LocalFocusManager.current
    val keyboard = keyboardAsState().value


    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    BackPressHandler(
        onBack = {
            if (sheetState.isVisible) coroutineScope.launch {
                sheetState.hide()
            }

        }, enabled = sheetState.isVisible
    ) {


        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                sheetContent.invoke {
                    coroutineScope.launch {
                        if (!keyboard) manager.clearFocus()
                        if (sheetState.isVisible) sheetState.hide()
                    }
                }
            },
            content = {
                mainContent.invoke {
                    coroutineScope.launch {
                        if (sheetState.isVisible) sheetState.hide()
                        else {
                            if (keyboard) manager.clearFocus()
                            sheetState.show()
                        }
                    }
                }
            },
            sheetShape = RoundedCornerShape(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
                topStart = 4.dp,
                topEnd = 4.dp,
            ),

            )
    }


}