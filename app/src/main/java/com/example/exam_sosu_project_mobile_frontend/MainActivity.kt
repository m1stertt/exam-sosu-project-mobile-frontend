package com.example.exam_sosu_project_mobile_frontend

import android.os.Bundle
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
            if (usernameEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            ) {
                val toastMessage = "Username: " + usernameEditText.text.toString() + ", Password: " + passwordEditText.text.toString()
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_SHORT).show()
            } else {
                val toastMessage = "Username or Password are not populated"
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_SHORT).show()
            }
            val apiInterface = LoginServiceInterface.create().getLogin()

            //apiInterface.enqueue( Callback<List<Movie>>())
            apiInterface.enqueue( object : Callback<List<Login>> {
                override fun onResponse(call: Call<List<Login>>?, response: Response<List<Login>>?) {

                    //if(response?.body() != null)
                    //    recyclerAdapter.setMovieListItems(response.body()!!)
                }

                override fun onFailure(call: Call<List<Login>>?, t: Throwable?) {

                }
            })
        }
    }
}