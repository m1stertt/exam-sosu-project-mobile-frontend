package com.example.exam_sosu_project_mobile_frontend

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.exam_sosu_project_mobile_frontend.databinding.ActivityTeacherBinding
import com.example.exam_sosu_project_mobile_frontend.entities.Citizen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTeacherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_teacher)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val apiInterface = ApiInterface.create(this);

        apiInterface.getCitizens().enqueue(object: Callback<List<Citizen>> {
            override fun onResponse(call: Call<List<Citizen>>?, response: Response<List<Citizen>>?) {

                Log.d("GetCitizen",response.toString())
                if (response != null) {
                    Log.d("GetCitizen",response.body().toString())
                }
            }
            override fun onFailure(call: Call<List<Citizen>>?, t: Throwable?) {
                Log.d("GetCitizen","error",t)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_teacher)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}