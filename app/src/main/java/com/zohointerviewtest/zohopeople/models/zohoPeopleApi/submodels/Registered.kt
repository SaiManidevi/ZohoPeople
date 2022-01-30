package com.zohointerviewtest.zohopeople.models.zohoPeopleApi.submodels

import androidx.room.ColumnInfo

data class Registered(
    @ColumnInfo(name = "reg_age")
    val age: Int,
    @ColumnInfo(name = "reg_date")
    val date: String
)