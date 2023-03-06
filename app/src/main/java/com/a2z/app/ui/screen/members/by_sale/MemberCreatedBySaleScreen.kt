package com.a2z.app.ui.screen.members.by_sale

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.a2z.app.ui.component.BaseContent
import com.a2z.app.ui.component.common.NavTopBar
import com.a2z.app.ui.screen.members.list.MemberFilterDialog
import com.a2z.app.ui.screen.report.component.ReportNavActionButton
import com.a2z.app.ui.theme.BackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MemberCreatedBySaleScreen() {

    val viewModel: MemberCreatedBySaleViewModel = hiltViewModel()
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = {
            NavTopBar(
                title = "Users Created By You",
                subTitle = viewModel.subTitle,
                actions = { ReportNavActionButton() {
                    viewModel.showFilterDialogState.value = true
                }}
            )
        }
    ) {

        BaseContent(viewModel) {
            if (viewModel.isComplete) MemberCompletedList()
            else MemberInCompletedList()
        }

        MemberFilterDialog(
            showDialogState = viewModel.showFilterDialogState,
            onFilter = { type, input ->
                viewModel.searchType = type
                viewModel.searchInput = input
                if(viewModel.isComplete){
                    viewModel.completePagingState.refresh()
                    viewModel.fetchCompletedUsers()
                }
                else{
                    viewModel.inCompletePagingState.refresh()
                    viewModel.fetchInCompletedUsers()
                }
            })

    }

}