package com.a2z.app.ui.util

import com.a2z.app.util.VoidCallback


data class AppPagingConfig(
    val maxPage: Int = 10,
    val listSize: Int = 30
)

data class PagingState<T>(
    var pagingConfig: AppPagingConfig = AppPagingConfig(),
    var items: List<T> = emptyList(),
    var isLoading: Boolean = false,
    var exception: Exception? = null,
    var page: Int = 1,
    private var endReached: Boolean = false,
) {


    fun refresh(config: AppPagingConfig? = null) {
        isLoading = false
        items = emptyList()
        exception = null
        endReached = false
        page = 1
        config?.let { pagingConfig = config }
    }

    fun successState(reports: List<T>, nextPage: Int?): PagingState<T> {

        var mEndReached = false
        if (reports.isEmpty()) mEndReached = true
        if (reports.size < this.pagingConfig.listSize) mEndReached = true
        if (nextPage == null || nextPage >= pagingConfig.maxPage) mEndReached = true


        return this.copy(
            isLoading = false,
            items = this.items + reports,
            endReached = mEndReached,
            page = nextPage ?: 0
        )
    }

    fun successState(reports: List<T>): PagingState<T> {

        this.page = this.page.plus(1)

        var mEndReached = false
        if (reports.isEmpty()) mEndReached = true
        if (reports.size < this.pagingConfig.listSize) mEndReached = true
        if ( page >= pagingConfig.maxPage) mEndReached = true

        return this.copy(
            isLoading = false,
            items = this.items + reports,
            endReached = mEndReached,
            page = page
        )
    }

    fun loadingState(): PagingState<T> {
        return this.copy(isLoading = true)
    }

    fun failureState(exception: Exception): PagingState<T> {
        return this.copy(
            exception = exception,
            isLoading = false
        )
    }

    fun shouldLoadNext(index: Int, callback: VoidCallback) {
        if (index >= this.items.size - 1 && !this.endReached
            && !this.isLoading
        ) {
            callback.invoke()
        }
    }

}