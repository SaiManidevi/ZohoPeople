package com.zohointerviewtest.zohopeople.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zohointerviewtest.zohopeople.data.remote.zohopeople.PeopleApiHelper
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.PeopleResult
import retrofit2.HttpException
import java.io.IOException

class PeopleRemotePagingSource(private val repository: PeopleRepository) :
    PagingSource<Int, PeopleResult>() {

    override fun getRefreshKey(state: PagingState<Int, PeopleResult>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PeopleResult> {
        return try {
            val nextPageNumber = params.key ?: DEFAULT_INIT_PAGE
            val response = repository.getZohoPeople(limit = LIMIT, nextPage = nextPageNumber)
            LoadResult.Page(
                data = response.body()?.results ?: emptyList(),
                prevKey = null,
                nextKey = if (response.body()?.info?.page == PAGE_END_LIMIT) null else nextPageNumber + 1
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

        /*val limit = LIMIT
        val page = params.key ?: DEFAULT_INIT_PAGE
        return try {
            val response = repository.getZohoPeople(limit = limit, nextPage = page)
            Log.d("TEST", "Response: ${response.body()}")
            LoadResult.Page(
                prevKey = if (page == DEFAULT_INIT_PAGE) null else page - 1,
                nextKey = if (response.body()?.info?.page == PAGE_END_LIMIT) null else page + 1,
                data = response.body()?.results ?: emptyList()
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }*/
    }

    companion object {
        const val DEFAULT_INIT_PAGE = 1
        const val LIMIT = 25
        const val PAGE_END_LIMIT = 100
    }
}