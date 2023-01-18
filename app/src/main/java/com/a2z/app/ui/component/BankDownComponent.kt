package com.a2z.app.ui.component

import android.view.LayoutInflater
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.R
import com.a2z.app.data.model.dmt.BankDownResponse
import com.a2z.app.ui.dialog.BankDownDialog
import com.a2z.app.ui.screen.home.HomeViewModel
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.PrimaryColor
import com.a2z.app.ui.theme.RedColor

@Composable
fun BankDownComponent(
    bankDownResponseState: MutableState<BankDownResponse?>
) {

    val bankDown = bankDownResponseState.value
    val isBankDown = bankDown != null && bankDown.status == 1

    val bankDialogState = remember {
        mutableStateOf(false)
    }

    if (!isBankDown) return

    BankDownDialog(dialogState = bankDialogState, bankList = bankDown?.bankList)


    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .clip(MaterialTheme.shapes.small)
            .background(BackgroundColor, shape = MaterialTheme.shapes.small)
            .padding(8.dp)
    ) {


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Bank Down",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.weight(1f))

            Row()
            {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    Modifier.size(16.dp), tint = PrimaryColor
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Show All", fontSize = 12.sp, color = PrimaryColor)
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Icon(
                imageVector = Icons.Default.AccountBalance, contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = RedColor
            )

            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                AndroidView(
                    factory = {
                        val view =
                            LayoutInflater.from(it)
                                .inflate(R.layout.layout_marquee, null, false)

                        val textView = view.findViewById<TextView>(R.id.tv_bank_down)
                        textView.apply {
                            this.text = bankDown?.bankString
                            this.isSelected = true
                        }

                        view
                    }, modifier = Modifier
                        .width(this.maxWidth)
                        .height(this.maxHeight)
                )
            }
        }
    }

}