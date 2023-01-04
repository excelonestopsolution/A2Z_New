package com.a2z.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.a2z.app.data.model.provider.Operator
import com.a2z.app.data.model.utility.RechargeTransactionResponse



class UtilityShareModel {

    lateinit var operator: Operator
    lateinit var rechargeResponse : RechargeTransactionResponse

}












val myList = listOf(1,2,3,4,5,6,7,8,)

@Preview
@Composable
fun MyListComponent() {

  Card(
      shape = CircleShape,
      backgroundColor = MaterialTheme.colors.primary
  ) {

      CommonHeadingComponent()


  }
}


@Composable
fun CommonHeadingComponent() {
    Text(text = "Heading", style = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,

        ))
}



















