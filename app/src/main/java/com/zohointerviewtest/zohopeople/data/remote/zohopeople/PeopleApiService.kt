package com.zohointerviewtest.zohopeople.data.remote.zohopeople

import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.ZohoPeopleResult
import com.zohointerviewtest.zohopeople.utils.Constants.PATH_PEOPLE_API
import com.zohointerviewtest.zohopeople.utils.Constants.QUERY_PARAM_PEOPLE_PAGE
import com.zohointerviewtest.zohopeople.utils.Constants.QUERY_PARAM_PEOPLE_RESULTS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PeopleApiService {
    // https://randomuser.me/api/?results=25&page=1
    @GET(PATH_PEOPLE_API)
    suspend fun getZohoPeople(
        @Query(QUERY_PARAM_PEOPLE_RESULTS) limit: Int,
        @Query(QUERY_PARAM_PEOPLE_PAGE) nextPage: Int
    ): Response<ZohoPeopleResult>
}