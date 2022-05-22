package com.example.exam_sosu_project_mobile_frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment: SupportMapFragment? = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    /**
        val i = Intent(this@WhateverActivity, MapsActivity::class.java)
        i.putExtra("latitude",latitude);
        i.putExtra("longitude",longitude);
        startActivity(i);
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        val extras = intent.extras ?: return
        val address=extras.getString("address")
        val lat1 = extras.get("latitude")
        val lng1 = extras.get("longitude")
        var lat=0.0
        var lng=0.0
        if(lat1 is String&&lng1 is String){
            lat=lat1.toDouble()
            lng=lng1.toDouble()
        }else if(lat1 is Double&&lng1 is Double){
            lat=lat1
            lng=lng1
        }
        mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(lat,lng))
                .title(address)
        )
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat,lng)))
    }
}

