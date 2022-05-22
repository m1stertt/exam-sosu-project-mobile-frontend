package com.example.exam_sosu_project_mobile_frontend.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Citizen(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "birthday") val birthday: String,
    @ColumnInfo(name = "civilStatus") val civilStatus: String,
    @Embedded val address: Address,
) : Serializable
