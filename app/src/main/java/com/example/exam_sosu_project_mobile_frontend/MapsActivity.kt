package com.example.exam_sosu_project_mobile_frontend

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.exam_sosu_project_mobile_frontend.databinding.ActivityMapsBinding
import com.example.exam_sosu_project_mobile_frontend.ui.citizens.CitizenViewActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding
    private var locationMarker:Marker? = null

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
    i.putExtra("address",string)
    i.putExtra("latitude",latitude);
    i.putExtra("longitude",longitude);
    startActivity(i);
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        val extras = intent.extras ?: return
        val address = extras.getString("address")
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
        title = address ?: "$lat, $lng"
        val destination=googleMap.addMarker(MarkerOptions().position(LatLng(lat, lng)).title(address))
        googleMap.setOnMarkerClickListener {
            val cu=CameraUpdateFactory.newLatLngZoom(it.position,16f)
            googleMap.animateCamera(cu)
            false
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lng)))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16f))
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        requestPermissions()
        if (!isPermissionGiven()) return binding.map.onResume()
        startListening()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        if (location != null) {
            val builder = LatLngBounds.Builder()
            if (destination != null) {
                builder.include(destination.position)
            }
            builder.include(LatLng(location.latitude,location.longitude))
            val bounds = builder.build()
            var initial=false
            binding.map.viewTreeObserver.addOnGlobalLayoutListener {
                if(initial) return@addOnGlobalLayoutListener
                initial=true
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,100))
            }

            locationMarker=googleMap.addMarker(
                MarkerOptions().position(LatLng(location.latitude,location.longitude)).title("Your location").icon(
                    BitmapDescriptorFactory.fromResource(android.R.drawable.presence_online))
            )
        }else{
            Toast.makeText(applicationContext, "Unable to retrieve location", Toast.LENGTH_SHORT).show()
        }
        binding.map.onResume()
    }

    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    private fun requestPermissions() {
        if (!isPermissionGiven()) {
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

    private var myLocationListener: LocationListener? = null

    @SuppressLint("MissingPermission")
    private fun startListening() {
        if (!isPermissionGiven())
            return

        if (myLocationListener == null)
            myLocationListener = object : LocationListener {

                override fun onLocationChanged(location: Location) {
                    if(locationMarker!=null){
                        locationMarker!!.position= LatLng(location.latitude,location.longitude)
                    }
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

