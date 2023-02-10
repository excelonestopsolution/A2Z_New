package com.a2z.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.a2z.app.BaseViewModel
import com.a2z.app.dist.data.repository.HomeRepository
import com.a2z.app.model.BalanceResponse
import com.a2z.app.util.ui.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
        private val homeRepository: HomeRepository
) : BaseViewModel() {

    private val _balanceObs = MutableLiveData<Resource<BalanceResponse>>()
    val balanceObs: LiveData<Resource<BalanceResponse>> = _balanceObs


    fun fetchBalanceInfo() {
        _balanceObs.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiRequest {
                        homeRepository.fetchBalanceInfo()
                    }
                }
                _balanceObs.value = Resource.Success(response)
            } catch (e: Exception) {
                _balanceObs.value = Resource.Failure(e)
            }
        }
    }
}