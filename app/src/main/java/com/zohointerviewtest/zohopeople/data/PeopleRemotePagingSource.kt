package com.zohointerviewtest.zohopeople.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zohointerviewtest.zohopeople.data.remote.zohopeople.PeopleApiHelper
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.PeopleResult
import retrofit2.HttpException
import java.io.IOException

class PeopleRemotePagingSource(private val apiHelper: PeopleApiHelper) :
    PagingSource<Int, PeopleResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PeopleResult> {
        return try {
            val nextPageNumber = params.key ?: DEFAULT_INIT_PAGE
            val response = apiHelper.getZohoPeople(limit = LIMIT, nextPage = nextPageNumber)
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
    }

    override fun getRefreshKey(state: PagingState<Int, PeopleResult>): Int? {
        return null
    }

    companion object {
        const val DEFAULT_INIT_PAGE = 1
        const val LIMIT = 25
        const val PAGE_END_LIMIT = 100
    }
}