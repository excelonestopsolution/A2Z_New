package com.a2z_di.app.activity.register.user

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.a2z_di.app.BaseViewModel
import com.a2z_di.app.activity.register.paging.CompleteUserPagingSource
import com.a2z_di.app.activity.register.paging.InCompleteUserPagingSource
import com.a2z_di.app.data.repository.RegistrationRepository
import com.a2z_di.app.data.response.RegisterCompleteUser
import com.a2z_di.app.data.response.RegisterInCompleteUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RegisterUserListingViewModel @Inject constructor(
    private val registerRepository: RegistrationRepository
) : BaseViewModel() {

    var inputMode =""
    var inputText =""

    fun fetchCompleteUsers(): Flow<PagingData<RegisterCompleteUser>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
            ),
            pagingSourceFactory = {
                CompleteUserPagingSource(
                    registerRepository,
                    hashMapOf(
                        "searchType" to inputMode,
                        "searchInput" to inputText
                    )
                )
            }
        ).flow
    }


    fun fetchInCompleteUsers(): Flow<PagingData<RegisterInCompleteUser>> {

        return Pager(
            config = PagingConfig(
                pageSize = 30,
            ),
            pagingSourceFactory = {
                InCompleteUserPagingSource(
                    registerRepository,
                    hashMapOf(
                        "searchType" to inputMode,
                        "searchInput" to inputText
                    )
                )
            }
        ).flow

    }
}