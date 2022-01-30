package com.zohointerviewtest.zohopeople.data.remote.zohopeople

import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.ZohoPeopleResult
import retrofit2.Response

interface PeopleApiHelper {
    suspend fun getZohoPeople(limit: Int, nextPage: Int): Response<ZohoPeopleResult>
}