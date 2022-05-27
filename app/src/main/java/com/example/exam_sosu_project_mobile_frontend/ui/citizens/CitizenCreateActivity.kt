package com.example.exam_sosu_project_mobile_frontend.ui.citizens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.exam_sosu_project_mobile_frontend.interfaces.ApiInterface
import com.example.exam_sosu_project_mobile_frontend.ui.DatePickerFragment
import com.example.exam_sosu_project_mobile_frontend.interfaces.DawaApiInterface
import com.example.exam_sosu_project_mobile_frontend.databinding.ActivityCitizenCreateBinding
import com.example.exam_sosu_project_mobile_frontend.entities.Citizen
import com.example.exam_sosu_project_mobile_frontend.entities.DawaAutoCompleteAddress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CitizenCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitizenCreateBinding
    lateinit var adapter: ArrayAdapter<DawaAutoCompleteAddress>
    var running:Boolean=false
    var addressClick:Boolean=false
    private val address= CitizenAddressDto("Spangsbjerg Kirkevej 103",6700)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitizenCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter=ArrayAdapter<DawaAutoCompleteAddress>(this@CitizenCreateActivity,android.R.layout.simple_list_item_1,ArrayList())
        //setSupportActionBar(binding.toolbar)
        binding.editDateBtn.setOnClickListener {
            showDatePickerDialog()
        }
        binding.createBtn.setOnClickListener {
            createCitizen()
        }
        binding.addressTextView.doAfterTextChanged {
            if(!addressClick) {
                Log.d("Change", binding.addressTextView.text.toString())
                if (binding.addressTextView.text.length > 3) {
                    dawaAutocomplete()
                } else {
                    adapter.clear()
                }
            }else{
                adapter.clear()
                addressClick=false
            }
        }
        binding.addressTextView.setAdapter(adapter)
        binding.addressTextView.onItemClickListener=object:AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(adapter.getItem(p2)==null) return
                addressClick=true
                address.street=adapter.getItem(p2)!!.adresse.vejnavn+" "+adapter.getItem(p2)!!.adresse.husnr
                address.postCode=adapter.getItem(p2)!!.adresse.postnr.toInt()
                binding.addressTextView.setText(address.street)
            }

        }
    }

    private fun createCitizen(){
        val apiInterface = ApiInterface.create(this)
        val citizen=CreateCitizenDto(binding.editTextFirstName.text.toString(),
            binding.editTextLastName.text.toString(),
            binding.editDateBtn.text.toString(),
            binding.editTextCivilStatus.text.toString(),address)
        apiInterface.createCitizen(citizen
        ).enqueue(object : Callback<Citizen> {
            override fun onResponse(call: Call<Citizen>?, response: Response<Citizen>?) {
                Log.d("CreateCitizen",response.toString())
                Log.d("CreateCitizen",response?.body().toString())
            }
            override fun onFailure(call: Call<Citizen>?, t: Throwable?) {
                Toast.makeText(applicationContext,"Issue creating the citizen.", Toast.LENGTH_SHORT).show()
                Log.d("CreateCitizenActivity","onFailure")
            }
        })
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
        supportFragmentManager
            .setFragmentResultListener("time", this) { time, bundle ->
                val result = bundle.getLong("bundleKey")
                binding.editDateBtn.text=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Date(result))
            }
    }

    private fun dawaAutocomplete(){
        if(running) return
        running=true
        //adapter.clear()
        val dawaApiInterface = DawaApiInterface.create(this)
        runOnUiThread {
            dawaApiInterface.autoComplete(binding.addressTextView.text.toString()).enqueue(object :
                Callback<List<DawaAutoCompleteAddress>> {
                override fun onResponse(call: Call<List<DawaAutoCompleteAddress>>?, response: Response<List<DawaAutoCompleteAddress>>?) {
                    Log.d("Response",response?.code().toString())
                    if (response?.body() != null) {
                        adapter.clear()
                        adapter.addAll(response.body()!!)
                    }else{
                        //adapter.clear();
                    }
                    running=false
                }
                override fun onFailure(call: Call<List<DawaAutoCompleteAddress>>?, t: Throwable?) {
                    adapter.clear()
                    Log.d("CitizenCreateActivity","onFailure"+t.toString())
                    running=false
                }
            })
        }

    }
}