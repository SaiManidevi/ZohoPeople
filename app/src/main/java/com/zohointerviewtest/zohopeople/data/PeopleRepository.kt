package com.zohointerviewtest.zohopeople.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zohointerviewtest.zohopeople.data.local.AppDatabase
import com.zohointerviewtest.zohopeople.data.local.zohopeople.PersonDao
import com.zohointerviewtest.zohopeople.data.local.zohopeople.RemoteKeysDao
import com.zohointerviewtest.zohopeople.data.remote.zohopeople.PeopleApiHelper
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.PeopleResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeopleRepository @Inject constructor(
    private val apiHelper: PeopleApiHelper,
    private val appDatabase: AppDatabase,
    private val personDao: PersonDao,
    private val remoteKeysDao: RemoteKeysDao
) {

    @ExperimentalPagingApi
    fun getZohoPeopleDb(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<PeopleResult>> {
        val pagingSourceFactory = { personDao.getAllPersons() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = ZohoPeopleRemoteMediator(apiHelper, appDatabase, personDao, remoteKeysDao)
        ).flow
    }

    fun getZohoPeople(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<PeopleResult>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PeopleRemotePagingSource(apiHelper = apiHelper) }
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            enablePlaceholders = false
        )
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 2
    }

}