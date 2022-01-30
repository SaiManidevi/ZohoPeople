package com.zohointerviewtest.zohopeople.models.zohoPeopleApi.submodels

import androidx.room.ColumnInfo

data class Street(
    @ColumnInfo(name = "street_name")
    val name: String,
    val number: Int
)