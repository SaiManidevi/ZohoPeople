package com.zohointerviewtest.zohopeople.models.zohoPeopleApi

import androidx.room.*
import com.zohointerviewtest.zohopeople.models.zohoPeopleApi.submodels.*

@Entity(tableName = "zohopeople")
data class PeopleResult(
    val cell: String,
    @Embedded
    val dob: Dob,
    val email: String,
    val gender: String,
    @PrimaryKey
    @Embedded
    val id: Id,
    @Embedded
    val location: Location,
    @Embedded
    val login: Login,
    @Embedded
    val name: Name,
    val nat: String,
    val phone: String,
    @Embedded
    val picture: Picture,
    @Embedded
    val registered: Registered
)