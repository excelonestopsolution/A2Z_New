package com.a2z.app.ui.screen.util.complaint

import com.a2z.app.data.local.AppPreference
import com.a2z.app.ui.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComplainViewModel @Inject constructor(
    private val appPreference: AppPreference
): BaseViewModel() {
}