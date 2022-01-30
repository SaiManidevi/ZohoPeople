package com.zohointerviewtest.zohopeople.data

/*
@ExperimentalPagingApi
class ZohoPeopleRemoteMediator(
    private val apiHelper: PeopleApiHelper,
    private val personDao: PersonDao,
    private val remoteKeysDao: RemoteKeysDao
) : RemoteMediator<Int, Person>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Person>): MediatorResult {
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
            if (loadType == LoadType.REFRESH) {
                remoteKeysDao.clearAllRemoteKeys()
                personDao.clearAllPeople()
            }
            val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
            val nextKey = if (isEndOfList == true) null else page + 1
            val keys = RemoteKeys(id = response.body()?.info?.seed ?: "", prevKey = prevKey, nextKey = nextKey)
            remoteKeysDao.insertKey(keys)
            val resultsList: List<> = response.body()
        }
    }

    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Person>): Any? {
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
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }

    */
/**
     * returns RemoteKeys for the loadType=LoadType.PREPEND. It basically gets the first page from PagingState and queries the database with the id of the DoggoImageModel
     *//*

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Person>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { person ->
                remoteKeysDao.getRemoteKeysById(person.id)
            }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Person>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { person ->
                remoteKeysDao.getRemoteKeysById(person.id)
            }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, Person>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeysById(id)
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val LIMIT = 25
        const val PAGE_END_LIMIT = 10000
    }
}*/
