package com.zohointerviewtest.zohopeople.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.zohointerviewtest.zohopeople.data.local.AppDatabase
import com.zohointerviewtest.zohopeople.data.local.zohopeople.PersonDao
import com.zohointerviewtest.zohopeople.data.local.zohopeople.RemoteKeys
import com.zohointerviewtest.zohopeople.data.local.zohopeople.RemoteKeysDao
import com.zohointerviewtest.zohopeople.data.remote.zohopeople.PeopleApiHelper
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.PeopleResult
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class ZohoPeopleRemoteMediator(
    private val apiHelper: PeopleApiHelper,
    private val appDatabase: AppDatabase,
    private val personDao: PersonDao,
    private val remoteKeysDao: RemoteKeysDao
) : RemoteMediator<Int, PeopleResult>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PeopleResult>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> DEFAULT_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(true)
            LoadType.APPEND -> {
                // this can run with an outdated PagingState from the previous RemoteMediator instance, causing the Exception to be thrown
                val remoteKeys = getLastRemoteKey(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }

        }
        return try {

            val response = apiHelper.getZohoPeople(limit = LIMIT, page)
            val endOfPaginationReached = response.body()?.results?.isEmpty()

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    personDao.clearAllPeople()
                    remoteKeysDao.clearAllRemoteKeys()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached == true) null else page + 1
                val keys = RemoteKeys(
                    id = response.body()?.info?.seed ?: "",
                    prevKey = prevKey,
                    nextKey = nextKey
                )
                remoteKeysDao.insertKey(keys)
                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                Log.d("TEST", "MED ${response.body()?.results}")
                val resultsList: List<PeopleResult> = response.body()?.results ?: emptyList()
                Log.d("TEST", "MED ${resultsList}")
                if (resultsList.isNotEmpty()) personDao.insertAll(resultsList)
            }
            MediatorResult.Success(
                endOfPaginationReached = response.body()?.info?.page == null
            )
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    /* override suspend fun load(
         loadType: LoadType,
         state: PagingState<Int, PeopleResult>
     ): MediatorResult {
         val pageKeyData = getKeyPageData(loadType, state)
         val page: Int = when (pageKeyData) {
             is MediatorResult.Success -> {
                 return pageKeyData
             }
             else -> {
                 pageKeyData as Int
             }
         }
         try {
             val response = apiHelper.getZohoPeople(limit = LIMIT, page)
             val isEndOfList = response.body()?.results?.isEmpty()
             appDatabase.withTransaction {
                 if (loadType == LoadType.REFRESH) {
                     remoteKeysDao.clearAllRemoteKeys()
                     personDao.clearAllPeople()
                 }
                 val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                 val nextKey = if (isEndOfList == true) null else page + 1
                 val keys = RemoteKeys(
                     id = response.body()?.info?.seed ?: "",
                     prevKey = prevKey,
                     nextKey = nextKey
                 )
                 remoteKeysDao.insertKey(keys)
                 Log.d("TEST", "MEDIATOR ${response.body()?.results}")
                 val resultsList: List<PeopleResult> = response.body()?.results ?: emptyList()
                 Log.d("TEST", "MEDIATOR ${resultsList.toString()}")
                 if (resultsList.isNotEmpty()) personDao.insertAll(resultsList)
             }
             return MediatorResult.Success(endOfPaginationReached = isEndOfList ?: true)
         } catch (exception: IOException) {
             return MediatorResult.Error(exception)
         } catch (exception: HttpException) {
             return MediatorResult.Error(exception)
         }
     }

     private suspend fun getKeyPageData(
         loadType: LoadType,
         state: PagingState<Int, PeopleResult>
     ): Any? {
         return when (loadType) {
             LoadType.REFRESH -> {
                 val remoteKeys = getClosestRemoteKey(state)
                 remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
             }
             LoadType.APPEND -> {
                 val remoteKeys = getLastRemoteKey(state)
                     ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                 remoteKeys.nextKey
             }
             LoadType.PREPEND -> {
                 val remoteKeys = getFirstRemoteKey(state)
                     ?: throw InvalidObjectException("Invalid state, key should not be null")
                 remoteKeys.prevKey
                     ?: return MediatorResult.Success(endOfPaginationReached = true)
                 remoteKeys.prevKey
             }
         }
     }
 */

    private suspend fun getFirstRemoteKey(state: PagingState<Int, PeopleResult>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { person ->
                remoteKeysDao.getRemoteKeysById(person.id.value)
            }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, PeopleResult>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { result ->
                remoteKeysDao.getRemoteKeysById(result.id.value)
            }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, PeopleResult>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeysById(id.value)
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val LIMIT = 25
        const val PAGE_END_LIMIT = 10000
    }
}
