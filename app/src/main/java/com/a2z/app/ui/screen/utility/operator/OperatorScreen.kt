package com.a2z.app.ui.screen.utility.operator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.data.model.provider.Operator
import com.a2z.app.data.model.provider.OperatorResponse
import com.a2z.app.nav.NavScreen
import com.a2z.app.ui.component.*
import com.a2z.app.ui.component.common.DropDownTextField
import com.a2z.app.ui.component.common.SearchTextField
import com.a2z.app.ui.dialog.SpinnerSearchDialog
import com.a2z.app.ui.screen.utility.util.OperatorType
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.LocalNavController
import com.a2z.app.util.Callback

@Composable
fun OperatorScreen(

) {

    val viewModel: OperatorViewModel = hiltViewModel()

    Scaffold(
        topBar = appBar(), backgroundColor = BackgroundColor
    ) {
        it.calculateBottomPadding()
        BaseContent(viewModel) {
            ObsComponent(viewModel.providerResponseObs) { data ->
                BuildProviderList(data, viewModel.operatorType)
            }
        }
        SpinnerSearchDialog(
            title = "Select Electricity State",
            state = viewModel.spinnerDialogState,
            list = viewModel.getStateList().map { it.second } as ArrayList<String>,
            initialSelectedValue = viewModel.selectedState.value) { state ->
            viewModel.onStateSelect(state)
        }
    }
}

@Composable
private fun appBar() = @Composable {
    val viewModel: OperatorViewModel = hiltViewModel()
    val util = viewModel.util
    NavTopBar(title = util.getOperatorTitle("Providers"))
}

@Composable
private fun BuildProviderList(operatorResponse: OperatorResponse, operatorType: OperatorType) {

    val providers = operatorResponse.providers ?: return
    val navController = LocalNavController.current
    val viewModel: OperatorViewModel = hiltViewModel()
    viewModel.operatorList = providers
    viewModel.operatorListObs.clear()
    viewModel.operatorListObs.addAll(providers)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column {


            if (operatorType == OperatorType.ELECTRICITY) DropDownTextField(
                value = viewModel.selectedState.value, hint = "Select State"
            ) {
                viewModel.spinnerDialogState.value = true
            }

            if (operatorType != OperatorType.PREPAID) SearchTextField(
                value = viewModel.searchValue.value,
                onQuery = {
                    viewModel.querySearch(it)
                },
                onClear = {
                    viewModel.clearSearch()
                })

            LazyColumn(
                Modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(viewModel.operatorListObs) {
                    ListItemComponent(item = it.apply {
                        serviceId = operatorResponse.serviceId ?: ""
                        baseUrl = operatorResponse.baseUrl ?: ""
                    }, onItemClick = { operator ->
                        if (operatorType == OperatorType.PREPAID || operatorType == OperatorType.DTH)
                            navController
                                .navigate(
                                    NavScreen.RechargeScreen.passArgs(
                                        operatorType = operatorType,
                                        operator = operator
                                    )
                                )
                        else navController
                            .navigate(
                                NavScreen.BillPaymentScreen.passArgs(
                                    operatorType = operatorType,
                                    operator = operator
                                )
                            )
                    })
                }
            }
        }
    }
}


@Composable
private fun ListItemComponent(
    item: Operator, onItemClick: Callback<Operator>
) {
    val viewModel: OperatorViewModel = hiltViewModel()

    val placeHolder =
        viewModel.util.getIconFromOperatorType()
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.clickable {
        onItemClick(item)
    }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 5.dp)
        ) {

            AppNetworkImage(
                url = item.baseUrl + item.providerImage,
                placeholderRes = placeHolder,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = item.operatorName ?: "",
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                ),
            )
        }
        Divider()
    }
}


