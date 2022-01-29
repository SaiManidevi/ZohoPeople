package com.zohointerviewtest.zohopeople.data

import java.lang.Exception

// Generic class to hold value with loading status
sealed class NetworkResult<out R>{
    data class Success<out T>(val data: T): NetworkResult<T>()
    data class Error(val exception: Exception): NetworkResult<Nothing>()
    object Loading: NetworkResult<Nothing>()

    /**
     * returns TRUE if [Result] is of type [Success] & holds non-null [Success.data]
     */
    val NetworkResult<*>.succeeded
    get() = this is Success && data != null
}
