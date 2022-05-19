package com.example.exam_sosu_project_mobile_frontend

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "role") val role: String?,
)
