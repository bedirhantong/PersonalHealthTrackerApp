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
import android.os.SystemClock
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.data.AddActivitiesAndShowToUser
import com.example.personalhealthtracker.databinding.ActivityTrackRunBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DecimalFormat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class TrackRunActivity : AppCompatActivity(), OnMapReadyCallback{

    lateinit var binding: ActivityTrackRunBinding
    private var myMap: GoogleMap? = null

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener : LocationListener

    var currentLocation : LatLng ?= null
    var prevLocation : LatLng? = null
    var totalDistance = 0.0

    var totalEnergyConsumption = 0.0
    var totalSteps = 0
    private var stopTimer:Long = 0
    var averageSpeed = 0.0

    var elapsedSecond = 0
    var formattedCalories = "0"
    var formattedDistance = ""


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission", "SetTextI18n", "CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackRunBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.totalDistance.text =  "0 km"
        binding.averagePace.text = "0"
        binding.energyConsump.text = "0"
        binding.totalStepNum.text = "0"

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val navigationBar = this.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.GONE



        binding.buttonToggleStart.setOnClickListener {
            binding.tvTimer.base = SystemClock.elapsedRealtime() + stopTimer
            binding.tvTimer.start()
            binding.buttonToggleStart.visibility = View.GONE
            binding.btnFinishRun.visibility = View.VISIBLE
            binding.btnToggleStop.visibility = View.VISIBLE


        }


        binding.btnToggleStop.setOnClickListener {
            stopTimer = binding.tvTimer.base - SystemClock.elapsedRealtime()
            binding.tvTimer.stop()
            binding.buttonToggleStart.visibility = View.VISIBLE
            binding.btnFinishRun.visibility = View.GONE
            binding.btnToggleStop.visibility = View.GONE
        }

        binding.btnFinishRun.setOnClickListener {
            val intent = Intent(this@TrackRunActivity, AddActivitiesAndShowToUser::class.java)
            intent.putExtra("sourceActivity", "Running Activity")
            startActivity(intent)
            this.finish()
        }
        supportActionBar?.hide()
    }


    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        // Latitude -> Enlem
        // Longtideu -> Boylam
        myMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener  = object : LocationListener {
            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
                if (provider == LocationManager.GPS_PROVIDER) {
                    // GPS devre dışı bırakıldığında yapılacak işlemler
                    showEnableGPSDialog()
                }
            }

            private fun showEnableGPSDialog() {
                // Kullanıcıya GPS'i etkinleştirmesi için bir uyarı mesajı gösterme
                val builder = AlertDialog.Builder(this@TrackRunActivity)
                builder.setTitle("GPS is Disabled")
                builder.setMessage("Please enable GPS to use this feature.")
                builder.setPositiveButton("Enable GPS") { _, _ ->
                    // GPS ayarlarına yönlendirme yapma
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    // İptal işlemi
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }

            @SuppressLint("MissingPermission")
            override fun onLocationChanged(p0: Location) {
                myMap?.clear()

                val decimalFormat = DecimalFormat("#.###")

                currentLocation = LatLng(p0.latitude,p0.longitude)
                currentLocation?.let { MarkerOptions().position(it).title("Current Location") }
                    ?.let { myMap?.addMarker(it) }
                currentLocation?.let { CameraUpdateFactory.newLatLngZoom(it,15f) }
                    ?.let { myMap?.moveCamera(it) }

                val dist: Double
                if (prevLocation != null){
                    dist = calculateDistance()
                    prevLocation = currentLocation
                } else{
                    prevLocation = currentLocation
                    dist = calculateDistance()
                }
                // TotalDistance
                totalDistance += dist

                // TotalSteps (I will handle it)
                totalSteps += (totalDistance/1000).toInt()

                formattedDistance = decimalFormat.format(totalDistance)

                elapsedSecond = ((SystemClock.elapsedRealtime()-(binding.tvTimer).base)/1000).toInt()
                totalEnergyConsumption += calculateCaloriesBurned(totalSteps,elapsedSecond,25)

                formattedCalories = decimalFormat.format(totalEnergyConsumption)
                averageSpeed = calculateAverageSpeed(totalSteps,elapsedSecond)


                binding.totalDistance.text =  "$formattedDistance km"
                binding.averagePace.text = "$averageSpeed"
                binding.energyConsump.text = formattedCalories
                binding.totalStepNum.text = totalSteps.toString()

                val sharedPreferences = getSharedPreferences("Bilgiler", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("activityType", "Running Activity")
                editor.putString("roadTravelled", formattedDistance)
                editor.putString("timeElapsed", elapsedSecond.toString())
                editor.putString("caloriesBurned", formattedCalories)
                editor.apply()

            }
        }

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //izin verilmemiş
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION),1)
        }else{
            // izin verilmiş
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1f, locationListener)
        }
    }


    fun calculateAverageSpeed(stepCount: Int, elapsedTime: Int): Double {
        val averageStepLength = 0.8 // Ortalama adım uzunluğu (metre)

        return (stepCount.toDouble() * averageStepLength) / (elapsedTime.toDouble() / 60)
    }

    fun calculateCaloriesBurned(stepCount: Int, elapsedTime: Int, age: Int): Double {
        val averageStepLength = 0.8 // Ortalama adım uzunluğu (metre)
        val walkingSpeed =
            (stepCount.toDouble() * averageStepLength) / (elapsedTime.toDouble()) // Yürüme hızı (metre/dakika)

        val caloriesPerMinute =
            calculateCaloriesPerMinute(walkingSpeed, age) // Dakika başına harcanan kalori hesapla

        return caloriesPerMinute * (elapsedTime.toDouble())
    }

    private fun calculateCaloriesPerMinute(walkingSpeed: Double, age: Int): Double {
        val MET = calculateMET(walkingSpeed) // Metabolik Eklenik Oranı (MET) hesapla
        val basalMetabolicRate =
            calculateBasalMetabolicRate(age) // Bazal Metabolik Hız (BMR) hesapla

        return MET * basalMetabolicRate / 24 / 60
    }

    private fun calculateMET(walkingSpeed: Double): Double {
        // Yürüme hızına göre MET değerini döndür (MET tablosundan alınabilir)
        return when {
            walkingSpeed < 3.2 -> 2.5
            walkingSpeed < 4.8 -> 3.0
            walkingSpeed < 6.4 -> 3.5
            walkingSpeed < 8.0 -> 4.3
            else -> 5.0
        }
    }

    private fun calculateBasalMetabolicRate(age: Int): Double {
        // Yaşa göre bazal metabolik hızı hesapla (BMR formülü kullanılabilir)
        return when {
            age < 3 -> 1000.0
            age < 10 -> 22.7 * age + 495.0
            age < 18 -> 17.5 * age + 651.0
            age < 30 -> 15.3 * age + 679.0
            age < 60 -> 11.6 * age + 879.0
            else -> 13.5 * age + 487.0
        }
    }



    private fun calculateDistance(): Double {
        val lastlat = prevLocation?.latitude ?: 0.0
        val lastlong = prevLocation?.longitude ?: 0.0
        val curLat = currentLocation?.latitude ?: 0.0
        val curLong = currentLocation?.longitude ?: 0.0

        return calculateDistance(curLat, curLong,lastlat , lastlong)
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371f // Dünya yarıçapı (km)

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.isNotEmpty()){
                if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    //izin verildi
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1f, locationListener)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}