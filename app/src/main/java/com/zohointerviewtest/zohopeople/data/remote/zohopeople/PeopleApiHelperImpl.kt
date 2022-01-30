package com.zohointerviewtest.zohopeople.data.remote.zohopeople

import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.ZohoPeopleResult
import retrofit2.Response
import javax.inject.Inject

class PeopleApiHelperImpl @Inject constructor(private val apiService: PeopleApiService) :
    PeopleApiHelper {
    override suspend fun getZohoPeople(limit: Int, nextPage: Int): Response<ZohoPeopleResult> {
        return apiService.getZohoPeople(limit = limit, nextPage = nextPage)
    }

}