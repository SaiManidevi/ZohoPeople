package com.zohointerviewtest.zohopeople.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zohointerviewtest.zohopeople.data.remote.zohopeople.PeopleApiHelper
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.PeopleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeopleRepository @Inject constructor(private val apiHelper: PeopleApiHelper) {

     suspend fun getZohoPeople(limit: Int, nextPage: Int) = withContext(Dispatchers.IO) {
         apiHelper.getZohoPeople(limit, nextPage)
     }

    /*fun getZohoPeople(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<PeopleResult>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PeopleRemotePagingSource(apiHelper) }
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            enablePlaceholders = false
        )
    }*/

    companion object {
        const val DEFAULT_PAGE_SIZE = 5
    }


}