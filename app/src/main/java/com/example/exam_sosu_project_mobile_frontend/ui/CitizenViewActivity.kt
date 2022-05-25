package com.example.exam_sosu_project_mobile_frontend.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.exam_sosu_project_mobile_frontend.ApiInterface
import com.example.exam_sosu_project_mobile_frontend.DatePickerFragment
import com.example.exam_sosu_project_mobile_frontend.DawaApiInterface
import com.example.exam_sosu_project_mobile_frontend.R
import com.example.exam_sosu_project_mobile_frontend.databinding.ActivityCitizenViewBinding
import com.example.exam_sosu_project_mobile_frontend.entities.Address
import com.example.exam_sosu_project_mobile_frontend.entities.Citizen
import com.example.exam_sosu_project_mobile_frontend.entities.DawaAddress
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*


class CitizenViewActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityCitizenViewBinding

    private lateinit var mSocket: Socket

    lateinit var mMap: GoogleMap

    lateinit var date:Date

    var x: Double=0.0
    var y: Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            mSocket = IO.socket("http://10.0.2.2:3000")
            mSocket.on("citizenUpdate", onCitizenUpdate)
            mSocket.connect()
        } catch (e: URISyntaxException) {
            //@todo
        }
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

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off("citizenUpdate", onCitizenUpdate)
    }

    private val onCitizenUpdate =
        Emitter.Listener { args ->
            this@CitizenViewActivity.runOnUiThread(Runnable {
                val data = args[0] as JSONObject
                val citizen=Gson().fromJson(data.toString(),Citizen::class.java);
                Log.d("onCitizenUpdate",citizen.toString())
                if(intent.extras!!.getInt("id")==data.getInt("id")){
                    date = SimpleDateFormat("yyyy-MM-dd").parse(citizen.birthday)
                    findViewById<EditText>(R.id.editTextFirstName).setText(citizen.firstName)
                    findViewById<EditText>(R.id.editTextLastName).setText(citizen.lastName)
                    findViewById<Button>(R.id.editDateBtn).text = citizen.birthday
                    findViewById<EditText>(R.id.editTextCivilStatus).setText(citizen.civilStatus)
                    dawaLookup(citizen.address)
                }
            })
        }


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("IMAGE","IMAGE")
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            findViewById<ImageView>(R.id.iconView).setImageBitmap(imageBitmap)

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

                    date = SimpleDateFormat("yyyy-MM-dd").parse(response.body()!!.birthday)
                    findViewById<EditText>(R.id.editTextFirstName).setText(response.body()!!.firstName)
                    findViewById<EditText>(R.id.editTextLastName).setText(response.body()!!.lastName)
                    findViewById<Button>(R.id.editDateBtn).text = response.body()!!.birthday
                    findViewById<EditText>(R.id.editTextCivilStatus).setText(response.body()!!.civilStatus)
                    dawaLookup(response.body()!!.address)
                }
            }
            override fun onFailure(call: Call<Citizen>?, t: Throwable?) {
                Log.d("StudentActivity","onFailure")
            }
        })
    }

    fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        val args = Bundle()
        args.putLong("time",date.time)
        newFragment.arguments=args
        newFragment.show(supportFragmentManager, "datePicker")
    }

    private fun dawaLookup(address: Address){
        val dawaApiInterface = DawaApiInterface.create(this)
        dawaApiInterface.get(address.street,address.postCode,"mini").enqueue(object : Callback<List<DawaAddress>> {
            override fun onResponse(call: Call<List<DawaAddress>>?, response: Response<List<DawaAddress>>?) {
                if (response?.body() != null) {
                    x=response.body()!![0].x
                    y=response.body()!![0].y
                    Log.d("CitizenViewActivity",mMap.toString())
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(y,x)))
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                    mMap.addMarker(MarkerOptions().title(address.street+", "+address.postCode).position(LatLng(y,x)))
                }
            }
            override fun onFailure(call: Call<List<DawaAddress>>?, t: Throwable?) {
                Log.d("CitizenViewActivity","onFailure"+t.toString())
            }
        })
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        p0.mapType = GoogleMap.MAP_TYPE_NORMAL
        findViewById<MapView>(R.id.mapView).onResume()
    }
}