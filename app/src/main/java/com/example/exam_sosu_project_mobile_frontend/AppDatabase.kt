package com.example.exam_sosu_project_mobile_frontend

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exam_sosu_project_mobile_frontend.dao.CitizenDao
import com.example.exam_sosu_project_mobile_frontend.entities.Citizen

@Database(entities = [Citizen::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun citizenDao(): CitizenDao
}