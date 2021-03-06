package com.example.exam_sosu_project_mobile_frontend.interfaces

import android.content.Context
import com.example.exam_sosu_project_mobile_frontend.entities.Login
import com.example.exam_sosu_project_mobile_frontend.R
import com.example.exam_sosu_project_mobile_frontend.entities.Citizen
import com.example.exam_sosu_project_mobile_frontend.ui.citizens.CreateCitizenDto
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*



interface ApiInterface {

    @GET("auth")
    fun getLogin(): Call<Login>

    @FormUrlEncoded
    @POST("auth")
    fun login(
        @Field("username") user: String,
        @Field("password") pass: String
    ): Call<Login>

    @POST("citizens")
    fun createCitizen(@Body() citizen: CreateCitizenDto): Call<Citizen>

    @GET("citizens")
    fun getCitizens(): Call<List<Citizen>>

    @GET("citizens/{id}")
    fun getCitizen(@Path("id") id: Int): Call<Citizen>

    companion object {

        private var BASE_URL = "http://10.0.2.2:3000"

        fun create(context:Context): ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("$BASE_URL/")
                .client(OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                        val token=sharedPreferences.getString("token",null)
                        val request = chain.request().newBuilder()
                        if(token!=null){
                            request.addHeader("cookie",token)
                        }
                        chain.proceed(request.build())
                    }.build()).build()
            return retrofit.create(ApiInterface::class.java)
        }
        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("$BASE_URL/").build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}