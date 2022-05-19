package com.example.exam_sosu_project_mobile_frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        val apiInterface = ApiInterface.create(this);
        val sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        val token=sharedPreferences.getString("token",null);
        val linearLayout:LinearLayout = findViewById(R.id.activity_main_linearLayout);
        if (token != null) {
            linearLayout.visibility=View.GONE;
            //Already logged in, attempt to retrieve user.
            apiInterface.getLogin().enqueue(object : Callback<Login> {
                override fun onResponse(call: Call<Login>?, response: Response<Login>?) {
                    if (response != null&&response.code()==200) {
                        intent= Intent(this@MainActivity, TeacherActivity::class.java);
                        startActivity(intent)
                        return;
                    }else{
                        sharedPreferences.edit().remove("token").commit();
                        initiate();
                    }
                }
                override fun onFailure(call: Call<Login>?, t: Throwable?) {
                    sharedPreferences.edit().remove("token").commit();
                    initiate();
                }
            })
        }

    }

    private fun initiate(){
        val linearLayout:LinearLayout = findViewById(R.id.activity_main_linearLayout);
        linearLayout.visibility=View.VISIBLE;
        val usernameEditText: EditText = findViewById(R.id.activity_main_usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.activity_main_passwordEditText)
        val loginButton: Button = findViewById(R.id.activity_main_loginButton)
        val apiInterface = ApiInterface.create(this);
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
                        var token= response.headers().get("Set-Cookie")?.substringBefore(';');
                        if (token != null) {
                            val sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                            sharedPreferences.edit().putString("token", token).commit();
                            intent= Intent(this@MainActivity, TeacherActivity::class.java);
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