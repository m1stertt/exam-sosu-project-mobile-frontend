package com.example.exam_sosu_project_mobile_frontend

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
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
        @Field("username") user: String?,
        @Field("password") pass: String?
    ): Call<Login>

    companion object {

        var BASE_URL = "http://10.0.2.2:3000"

        fun create(context:Context): ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("$BASE_URL/")
                .client(OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    val token=sharedPreferences.getString("token","");
                    val request: Request = chain.request().newBuilder()
                        .addHeader("cookie", token).build()
                    chain.proceed(request)
                }.build()).build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}