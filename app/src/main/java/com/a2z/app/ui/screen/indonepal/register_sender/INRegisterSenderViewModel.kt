package com.a2z.app.ui.screen.indonepal.register_sender

import com.a2z.app.data.repository.IndoNepalRepository
import com.a2z.app.ui.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class INRegisterSenderViewModel @Inject constructor(
    private val repository: IndoNepalRepository
) : BaseViewModel() {
}