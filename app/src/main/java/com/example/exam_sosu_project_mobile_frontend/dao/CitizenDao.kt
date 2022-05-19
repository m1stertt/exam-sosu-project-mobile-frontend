package com.example.exam_sosu_project_mobile_frontend.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.exam_sosu_project_mobile_frontend.entities.Citizen

@Dao
interface CitizenDao {
    @Query("SELECT * FROM citizen")
    fun getAll(): List<Citizen>

    @Query("SELECT * FROM citizen WHERE id IN (:citizenIds)")
    fun loadAllByIds(citizenIds: IntArray): List<Citizen>

    @Query("SELECT * FROM citizen WHERE firstName LIKE :first AND " +
            "lastName LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Citizen

    @Insert
    fun insertAll(vararg users: Citizen)

    @Delete
    fun delete(user: Citizen)

}