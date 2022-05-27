package com.example.exam_sosu_project_mobile_frontend

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.exam_sosu_project_mobile_frontend.databinding.ActivityMapsBinding
import com.example.exam_sosu_project_mobile_frontend.databinding.ActivityStudentBinding
import com.example.exam_sosu_project_mobile_frontend.ui.CitizenViewActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import kotlin.math.abs


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if(id == R.id.action_citizen){
            val extras = intent.extras ?: return super.onOptionsItemSelected(item)
            val intent = Intent(this@MapsActivity, CitizenViewActivity::class.java)
            intent.putExtra("id",extras.getInt("id"))
            startActivity(intent)
        }
        if (id == R.id.action_logout) {
            val sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("token").apply()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
    val i = Intent(this@WhateverActivity, MapsActivity::class.java)
    i.putExtra("latitude",latitude);
    i.putExtra("longitude",longitude);
    startActivity(i);
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        val extras = intent.extras ?: return
        val address = extras.getString("address")
        title = address
        val lat1 = extras.get("latitude")
        val lng1 = extras.get("longitude")
        var lat = 0.0
        var lng = 0.0
        if (lat1 is String && lng1 is String) {
            lat = lat1.toDouble()
            lng = lng1.toDouble()
        } else if (lat1 is Double && lng1 is Double) {
            lat = lat1
            lng = lng1
        }
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title(address)
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lng)))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16f))
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        //googleMap.uiSettings.
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                //buildGoogleApiClient();
                googleMap.isMyLocationEnabled=true
            }
        }
        else {
            googleMap.isMyLocationEnabled=true;
        }
        requestPermissions()
        if (!isPermissionGiven()) return binding.map.onResume()
        startListening()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        // The type of location is Location? - it can be null... handle cases

        if (location != null) {
            Log.d("MapsActivity",location.toString())
            val zoomRatio:Float= ((abs(lat-location.latitude)+abs(lng-location.longitude))/2*7.5).toFloat()
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(zoomRatio))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng((lat+location.latitude)/2,(lng+location.longitude)/2)))
        }
        binding.map.onResume()
    }

    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    private fun requestPermissions() {
        if (!isPermissionGiven()) {
            //Log.d(TAG, "permission denied to USE GPS - requesting it")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(permissions, 1)
        }
    }

    private fun isPermissionGiven(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return permissions.all { p -> checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED}
        }
        return true
    }

    var myLocationListener: LocationListener? = null

    @SuppressLint("MissingPermission")
    private fun startListening() {
        if (!isPermissionGiven())
            return

        if (myLocationListener == null)
            myLocationListener = object : LocationListener {

                override fun onLocationChanged(location: Location) {

                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }
            }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            0,
            0.0F,
            myLocationListener!!)

    }

    @SuppressLint("MissingPermission")
    private fun stopListening() {

        if (myLocationListener == null) return

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(myLocationListener!!)
    }

    override fun onStop(){
        stopListening()
        super.onStop()
    }
}

