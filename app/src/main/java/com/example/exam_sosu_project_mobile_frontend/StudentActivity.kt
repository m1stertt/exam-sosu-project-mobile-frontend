package com.example.exam_sosu_project_mobile_frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.exam_sosu_project_mobile_frontend.databinding.ActivityStudentBinding
import com.example.exam_sosu_project_mobile_frontend.entities.Citizen
import com.example.exam_sosu_project_mobile_frontend.interfaces.ApiInterface
import com.example.exam_sosu_project_mobile_frontend.ui.citizens.CitizenCreateActivity
import com.example.exam_sosu_project_mobile_frontend.ui.citizens.CitizenFragment
import com.example.exam_sosu_project_mobile_frontend.ui.citizens.MyCitizenRecyclerViewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        findViewById<Button>(R.id.createCitizenBtn).setOnClickListener {
            val intent= Intent(it.context, CitizenCreateActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.welcomeText).text = getString(R.string.welcome_message,getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("username",""))
        getCitizens()
    }

    private fun getCitizens(){
        val apiInterface = ApiInterface.create(this)
        apiInterface.getCitizens().enqueue(object : Callback<List<Citizen>> {
            override fun onResponse(call: Call<List<Citizen>>?, response: Response<List<Citizen>>?) {
                if(response?.body() != null&&response.code()==200){
                    Log.d("StudentActivity","getCitizens onResponse")
                    val fragment=supportFragmentManager.findFragmentById(R.id.fragment_container_view)
                    (((fragment as CitizenFragment).view as RecyclerView).adapter as MyCitizenRecyclerViewAdapter).updateData(
                        response.body()!!
                    )
                }else{
                    Toast.makeText(applicationContext,"Issue retrieving the citizens.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Citizen>>?, t: Throwable?) {
                Toast.makeText(applicationContext,"Issue retrieving the citizens.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_logout) {
            val sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("token").apply()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}