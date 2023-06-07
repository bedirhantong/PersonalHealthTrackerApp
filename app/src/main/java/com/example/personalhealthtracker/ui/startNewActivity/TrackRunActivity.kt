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
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
import java.text.DecimalFormat


class TrackRunActivity : AppCompatActivity(), OnMapReadyCallback{

    lateinit var binding: ActivityTrackRunBinding
    private var myMap: GoogleMap? = null
    lateinit var currentLocation : LatLng
    lateinit var lastKnowLatLng : LatLng

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener : LocationListener

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackRunBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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


    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        // Latitude -> Enlem
        // Longtideu -> Boylam
        myMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener  = object : LocationListener {
            @SuppressLint("MissingPermission")
            override fun onLocationChanged(p0: Location) {
                myMap?.clear()
                currentLocation = LatLng(p0.latitude,p0.longitude)

                myMap?.addMarker(MarkerOptions().position(currentLocation).title("Current Location"))
                myMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15f))

                var newDistance = 0.0


                val lastlat = lastKnowLatLng.latitude
                val lastlong = lastKnowLatLng.longitude
                val curLat = currentLocation.latitude
                val curLong = currentLocation.longitude

                newDistance += calculateDistance(lastlat,lastlong,curLat,curLong)

                val decimalFormat = DecimalFormat("#.###")
                val formattedDistance = decimalFormat.format(newDistance)

                binding.totalDistance.text = "Total Distance : $formattedDistance km"
                binding.averagePace.text = "Average speed : 0"
                binding.energyConsump.text = "Total energy consumption : 0"



            }
        }

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //izin verilmemiş
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION),1)
        }else{
            // izin verilmiş
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1f, locationListener)

            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastKnowLatLng = if (lastLocation != null){
                LatLng(lastLocation.latitude,lastLocation.longitude)
        //                myMap?.addMarker(MarkerOptions().position(lastKnowLatLng).title("Last known Location"))
        //                myMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnowLatLng,15f))
            }else{
                currentLocation // Güncellenmiş satır
            }


        }


    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371f // Dünya yarıçapı (km)

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
                kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(Math.toRadians(lat2)) *
                kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return earthRadius * c
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.size > 0){
                if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    //izin verildi
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1f, locationListener)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}