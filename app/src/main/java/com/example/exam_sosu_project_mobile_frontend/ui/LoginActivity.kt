package com.example.exam_sosu_project_mobile_frontend.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.exam_sosu_project_mobile_frontend.interfaces.ApiInterface
import com.example.exam_sosu_project_mobile_frontend.entities.Login
import com.example.exam_sosu_project_mobile_frontend.R
import com.example.exam_sosu_project_mobile_frontend.StudentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val apiInterface = ApiInterface.create(this)
        val sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val token=sharedPreferences.getString("token",null)
        if (token != null) { //Found a session token, attempt to retrieve user.
            apiInterface.getLogin().enqueue(object : Callback<Login> {
                override fun onResponse(call: Call<Login>?, response: Response<Login>?) {
                    if (response != null&&response.code()==200) {
                        intent= Intent(this@LoginActivity, StudentActivity::class.java)
                        startActivity(intent)
                        return
                    }else{
                        sharedPreferences.edit().remove("token").apply()
                        initiate()
                    }
                }
                override fun onFailure(call: Call<Login>?, t: Throwable?) {
                    sharedPreferences.edit().remove("token").apply()
                    initiate()
                }
            })
        }else{
            initiate()
        }
    }
    private fun initiate(){
        val apiInterface = ApiInterface.create(this)
        val usernameEditText: EditText = findViewById(R.id.activity_main_usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.activity_main_passwordEditText)
        val loginButton: Button = findViewById(R.id.activity_main_loginButton)
        loginButton.setOnClickListener{
            if (usernameEditText.text.isEmpty() && passwordEditText.text.isEmpty()
            ) {
                val toastMessage = "Please enter a username and password."
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_SHORT).show()
            }
            val apiInterfaceLogin=apiInterface.login(usernameEditText.text.toString(),passwordEditText.text.toString())
            apiInterfaceLogin.enqueue( object : Callback<Login> {
                override fun onResponse(call: Call<Login>?, response: Response<Login>?) {
                    if (response != null) {
                        val token= response.headers().get("Set-Cookie")?.substringBefore(';')
                        if (token != null) {
                            val sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                            val editor=sharedPreferences.edit()
                            editor.putString("token", token)
                            editor.putString("username", response.body()?.username)
                            editor.apply()
                            intent= Intent(this@LoginActivity, StudentActivity::class.java)
                            startActivity(intent)
                        }
                    }else{
                        //Failed login @todo
                    }
                }

                override fun onFailure(call: Call<Login>?, t: Throwable?) {
                    Log.d("Login","error",t)
                }
            })
        }
    }
}