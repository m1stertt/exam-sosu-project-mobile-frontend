package com.example.exam_sosu_project_mobile_frontend.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.io.Serializable

data class Address(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "street") val street: String,
    @ColumnInfo(name = "postCode") val postCode: Int,
    @ColumnInfo(name = "note") val note: String,
) : Serializable
