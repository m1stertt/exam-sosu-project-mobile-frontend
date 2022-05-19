package com.example.exam_sosu_project_mobile_frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val usernameEditText: EditText = findViewById(R.id.activity_main_usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.activity_main_passwordEditText)
        val loginButton: Button = findViewById(R.id.activity_main_loginButton)

        loginButton.setOnClickListener{
            if (usernameEditText.text.isEmpty() && passwordEditText.text.isEmpty()
            ) {
                val toastMessage = "Please enter a username and password."
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_SHORT).show()
            }
            val apiInterface = ApiInterface.create(this);
            val apiInterfaceLogin=apiInterface.login(usernameEditText.text.toString(),passwordEditText.text.toString())
            apiInterfaceLogin.enqueue( object : Callback<Login> {
                override fun onResponse(call: Call<Login>?, response: Response<Login>?) {

                    if (response != null) {
                        var token= response.headers().get("Set-Cookie")
                        if(token!=null){
                            token=token.toString().substringBefore(';');
                            Log.d("Login",token)
                            val sharedPreferences = getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

                            sharedPreferences.edit().putString("token",token).commit();

                            intent= Intent(this@MainActivity, TeacherActivity::class.java);
                            startActivity(intent)
                            apiInterface.getLogin().enqueue(object:Callback<Login>{
                                override fun onResponse(call: Call<Login>?, response: Response<Login>?) {
                                    Log.d("GetLogin",response.toString())
                                    if (response != null) {
                                        Log.d("GetLogin",response.body().toString())
                                    }
                                }
                                override fun onFailure(call: Call<Login>?, t: Throwable?) {
                                    Log.d("GetLogin","error",t)
                                }
                            })
                        }else{
                            //Failed login @todo
                        }
                    }
                }

                override fun onFailure(call: Call<Login>?, t: Throwable?) {
                    Log.d("Login","error",t)
                }
            })
        }
    }
}