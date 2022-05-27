package com.example.exam_sosu_project_mobile_frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.exam_sosu_project_mobile_frontend.ui.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent= Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}