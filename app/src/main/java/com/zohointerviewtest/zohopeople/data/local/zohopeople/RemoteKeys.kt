package com.zohointerviewtest.zohopeople.data.local.zohopeople

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(
    @PrimaryKey(autoGenerate = false) val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
