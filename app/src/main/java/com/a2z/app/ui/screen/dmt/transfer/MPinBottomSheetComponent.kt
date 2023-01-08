package com.a2z.app.ui.screen.dmt.transfer

import android.media.MediaParser.InputReader
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.ui.component.common.PinTextField
import com.a2z.app.ui.component.keyboardAsState
import com.a2z.app.ui.util.AppValidator
import com.a2z.app.ui.util.BaseInput
import com.a2z.app.ui.util.InputWrapper

@Composable
fun MPinBottomSheetComponent(onDone: (String) -> Unit) {
    val manager = LocalFocusManager.current
    val keyboard = keyboardAsState()
    val focusRequest = remember {
        FocusRequester()
    }
    val input = remember {
        MPinInput()
    }




    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .focusRequester(focusRequest)
    ) {
        Text(
            text = "Enter M-PIN",
            fontWeight = FontWeight.Bold, fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.padding(horizontal = 64.dp)) {
            PinTextField(
                value = input.mpin.getValue(),
                onChange = { input.mpin.onChange(it) },
                error = input.mpin.formError(),
                mpin = true,
                maxLength = 4,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (input.validate()) {
                if(!keyboard.value) manager.clearFocus()
                onDone.invoke(input.mpin.getValue())
            }
        }, shape = CircleShape) {
            Text(text = "   Proceed   ")
        }
    }

}

private data class MPinInput(
    val mpin: InputWrapper = InputWrapper { AppValidator.mpinValidation(it, length = 4) }
) : BaseInput(mpin)