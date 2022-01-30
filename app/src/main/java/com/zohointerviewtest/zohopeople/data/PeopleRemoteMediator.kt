package com.zohointerviewtest.zohopeople.data

/*
@ExperimentalPagingApi
class PeopleRemoteMediator(
    private val apiHelper: PeopleApiHelper,
    private val personDao: PersonDao,
    private val remoteKeysDao: RemoteKeysDao
) : RemoteMediator<Int, ZohoPeopleResult>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ZohoPeopleResult>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.APPEND -> {
                    val remoteKey = getLastKey(state) ?: throw InvalidObjectException("Exception")
                    remoteKey.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.REFRESH -> {
                    val remoteKey = getClosestKey(state)
                    remoteKey?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

    }

    suspend fun getClosestKey(state: PagingState<Int, Result>): RemoteKeys? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let { ApiResult ->
                remoteKeysDao.getRemoteKeysById(ApiResult.id.value)
            }
        }
    }

    suspend fun getLastKey(state: PagingState<Int, Result>): RemoteKeys? {
        return state.lastItemOrNull()?.let { ApiResult ->
            remoteKeysDao.getRemoteKeysById(ApiResult.id.value)
        }
    }
}
*/
