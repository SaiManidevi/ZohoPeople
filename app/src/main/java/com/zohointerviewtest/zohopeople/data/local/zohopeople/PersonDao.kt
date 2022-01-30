package com.zohointerviewtest.zohopeople.data.local.zohopeople

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.PeopleResult

@Dao
interface PersonDao {

    @Query("SELECT * FROM zohopeople")
    fun getAllPersons(): PagingSource<Int, PeopleResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(zohoPeopleList: List<PeopleResult>)

    @Query("DELETE FROM zohopeople")
    suspend fun clearAllPeople()
}
