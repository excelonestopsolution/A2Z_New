package com.a2z.app.ui.screen.util.complaint

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.a2z.app.data.model.report.LedgerReport
import com.a2z.app.data.network.ReportService
import kotlinx.coroutines.delay


class ComplaintPagingSource(
    private val reportService: ReportService,
    private val queryParam: HashMap<String, String>
) : PagingSource<Int, LedgerReport>() {
    override fun getRefreshKey(state: PagingState<Int, LedgerReport>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LedgerReport> {
        return try {
            queryParam["page"] = (params.key ?: FIRST_PAGE_INDEX).toString()
            val response = reportService.ledgerReport(data = queryParam)
             if (response.status == 1)
            {
                var nextPageNumber = if (response.nextPage == 0) null else response.nextPage
                val prevPageNumber = if (response.prevPage == 0) null else response.prevPage


                if (response.reports == null) {
                    nextPageNumber = null
                }

                if (response.reports!!.isEmpty()) {
                    nextPageNumber = null
                }
                if (response.reports!!.size < 30) {
                    nextPageNumber = null
                }

                if (response.nextPage == 30) {
                    nextPageNumber = null
                }

                LoadResult.Page(
                    data = response.reports!!,
                    prevKey = prevPageNumber,
                    nextKey = nextPageNumber
                )
            }

            else {
                LoadResult.Error(Exception("Error occurred!"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val FIRST_PAGE_INDEX = 1
    }

}