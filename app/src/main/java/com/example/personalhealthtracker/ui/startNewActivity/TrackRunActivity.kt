package com.example.personalhealthtracker.ui.startNewActivity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.ActivityTrackRunBinding
import com.example.personalhealthtracker.ui.LoginActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView


class TrackRunActivity : AppCompatActivity(), OnMapReadyCallback{

    lateinit var binding: ActivityTrackRunBinding
    private var myMap: GoogleMap? = null


    private lateinit var locationManager: LocationManager
    private lateinit var locationListener : LocationListener


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackRunBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //önceden kalmışsa mapteki işratleri siler
        myMap?.clear()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val navigationBar = this.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.GONE

        binding.btnToggleRun.setOnClickListener {
        }

        binding.btnFinishRun.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        // Latitude -> Enlem
        // Longtideu -> Boylam
        myMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener  = object : LocationListener {
            override fun onLocationChanged(p0: Location) {
                myMap?.clear()
                val currentLocation = LatLng(p0.latitude,p0.longitude)
                myMap?.addMarker(MarkerOptions().position(currentLocation).title("Current Location"))
                myMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15f))

            }
        }

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //izin verilmemiş
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION),1)
        }else{
            // izin verilmiş
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,1f,locationListener)
            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation != null){
                val lastKnowLatLng = LatLng(lastLocation.latitude,lastLocation.longitude)
                myMap?.addMarker(MarkerOptions().position(lastKnowLatLng).title("Last known Location"))
                myMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnowLatLng,15f))
            }
        }

    }

}