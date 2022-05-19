package com.example.exam_sosu_project_mobile_frontend

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Address(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "street") val street: String,
    @ColumnInfo(name = "postCode") val postCode: Int,
    @ColumnInfo(name = "note") val note: String,
)
