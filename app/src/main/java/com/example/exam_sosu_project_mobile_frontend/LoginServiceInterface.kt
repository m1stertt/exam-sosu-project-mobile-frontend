package com.example.exam_sosu_project_mobile_frontend

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory

interface LoginServiceInterface {

    @GET("volley_array.json")
    fun getLogin(): Call<List<Login>>

    companion object {

        var BASE_URL = "http://velmm.com/apis/"

        fun create(): LoginServiceInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(LoginServiceInterface::class.java)

        }
    }
}