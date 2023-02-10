package com.di_md.a2z.activity.register.user

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.di_md.a2z.BaseViewModel
import com.di_md.a2z.activity.register.paging.CompleteUserPagingSource
import com.di_md.a2z.activity.register.paging.InCompleteUserPagingSource
import com.di_md.a2z.data.repository.RegistrationRepository
import com.di_md.a2z.data.response.RegisterCompleteUser
import com.di_md.a2z.data.response.RegisterInCompleteUser
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