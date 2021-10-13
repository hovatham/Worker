package com.pushe.worker.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pushe.worker.data.model.Operation
import retrofit2.HttpException
import java.io.IOException

class OperationsDataSource (
    private val apiService: ERPRestService,
    private val userId: String
): PagingSource<Int, Operation>() {
    override fun getRefreshKey(state: PagingState<Int, Operation>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Operation> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 1
            val response = apiService.getOperations(
                userId = userId,
                skip = (nextPage-1)*params.loadSize,
                top = params.loadSize,
                orderby = "Выполнено desc"
            )
            return LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.isEmpty()) null else nextPage + 1
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }
}