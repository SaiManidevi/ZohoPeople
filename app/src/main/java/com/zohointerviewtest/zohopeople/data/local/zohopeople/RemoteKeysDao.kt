package com.zohointerviewtest.zohopeople.data.local.zohopeople

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(remoteKey: List<RemoteKeys>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKeys: RemoteKeys)

    @Query("SELECT * FROM remotekeys WHERE id = :id")
    suspend fun getRemoteKeysById(id: String): RemoteKeys?

    @Query("DELETE FROM remotekeys")
    suspend fun clearAllRemoteKeys()
}