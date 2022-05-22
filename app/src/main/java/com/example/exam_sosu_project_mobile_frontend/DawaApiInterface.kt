package com.example.exam_sosu_project_mobile_frontend

import android.content.Context
import com.example.exam_sosu_project_mobile_frontend.entities.DawaAddress
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DawaApiInterface {
    @GET("adresser")
    fun get(@Query("q") street: String,@Query("postnr") postCode: Int,@Query("struktur") structure: String): Call<List<DawaAddress>>

    companion object {

        private var BASE_URL = "https://api.dataforsyningen.dk"

        fun create(context: Context): DawaApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("$BASE_URL/")
                .build()
            return retrofit.create(DawaApiInterface::class.java)

        }
    }
}