package com.example.exam_sosu_project_mobile_frontend.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import com.example.exam_sosu_project_mobile_frontend.ApiInterface
import com.example.exam_sosu_project_mobile_frontend.DawaApiInterface
import com.example.exam_sosu_project_mobile_frontend.R
import com.example.exam_sosu_project_mobile_frontend.databinding.ActivityCitizenViewBinding
import com.example.exam_sosu_project_mobile_frontend.entities.Address
import com.example.exam_sosu_project_mobile_frontend.entities.Citizen
import com.example.exam_sosu_project_mobile_frontend.entities.DawaAddress
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitizenViewActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityCitizenViewBinding

    lateinit var mMap: GoogleMap

    var x: Double=0.0
    var y: Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitizenViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setSupportActionBar(binding.toolbar)

        val mapFragment = findViewById<MapView>(R.id.mapView)
        mapFragment?.onCreate(savedInstanceState)
        mapFragment?.getMapAsync(this)
        getCitizen()

        binding.addImageBtn.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            //val data: Intent? = result.data
            //@todo
        }
    }
    private fun dispatchTakePictureIntent() {
        try {
            resultLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            //@todo
        }
    }

    private fun getCitizen(){
        val apiInterface = ApiInterface.create(this)
        apiInterface.getCitizen(intent.extras!!.getInt("id")).enqueue(object : Callback<Citizen> {
            override fun onResponse(call: Call<Citizen>?, response: Response<Citizen>?) {
                if(response?.body() != null&&response.code()==200){
                    findViewById<EditText>(R.id.editTextFirstName).setText(response.body()!!.firstName)
                    findViewById<EditText>(R.id.editTextLastName).setText(response.body()!!.lastName)
                    findViewById<EditText>(R.id.editTextBirthday).setText(response.body()!!.birthday)
                    findViewById<EditText>(R.id.editTextCivilStatus).setText(response.body()!!.civilStatus)
                    dawaLookup(response.body()!!.address)
                }
            }
            override fun onFailure(call: Call<Citizen>?, t: Throwable?) {
                Log.d("StudentActivity","onFailure")
            }
        })
    }

    private fun dawaLookup(address: Address){
        val dawaApiInterface = DawaApiInterface.create(this)
        dawaApiInterface.get(address.street,address.postCode,"mini").enqueue(object : Callback<List<DawaAddress>> {
            override fun onResponse(call: Call<List<DawaAddress>>?, response: Response<List<DawaAddress>>?) {
                //
                if (response?.body() != null) {
                    x=response.body()!![0].x
                    y=response.body()!![0].y
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(y,x)))
                }
            }
            override fun onFailure(call: Call<List<DawaAddress>>?, t: Throwable?) {
                Log.d("CitizenViewActivity","onFailure"+t.toString())
            }
        })
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
    }
}