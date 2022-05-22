package com.example.exam_sosu_project_mobile_frontend.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.exam_sosu_project_mobile_frontend.R
import com.example.exam_sosu_project_mobile_frontend.databinding.ActivityCitizenViewBinding
import com.example.exam_sosu_project_mobile_frontend.databinding.ActivityStudentBinding

class CitizenViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitizenViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitizenViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }
}