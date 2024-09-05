//package com.example.personalhealthtracker.feature.startnewactivity.running
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.location.Location
//import android.location.LocationListener
//import android.location.LocationManager
//import android.os.Build
//import android.os.Bundle
//import android.os.SystemClock
//import android.provider.Settings
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AlertDialog
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.navigation.NavOptions
//import androidx.navigation.fragment.findNavController
//import com.example.personalhealthtracker.R
//import com.example.personalhealthtracker.databinding.FragmentTrackRunningBinding
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.gms.maps.model.Polyline
//import com.google.android.gms.maps.model.PolylineOptions
//import java.text.DecimalFormat
//import kotlin.math.atan2
//import kotlin.math.cos
//import kotlin.math.sin
//import kotlin.math.sqrt
//
//class TrackRunningFragment : Fragment(), OnMapReadyCallback {
//    private var _binding: FragmentTrackRunningBinding? = null
//    private val binding get() = _binding!!
//
//    private var myMap: GoogleMap? = null
//    private lateinit var locationManager: LocationManager
//    private lateinit var locationListener: LocationListener
//
//    private var currentLocation: LatLng? = null
//    private var prevLocation: LatLng? = null
//    private var totalDistance = 0.0
//    private var totalEnergyConsumption = 0.0
//    private var totalSteps = 0
//    private var stopTimer: Long = 0
//    private var averageSpeed = 0.0
//    private var elapsedSecond = 0
//    private var formattedCalories = "0"
//    private var formattedDistance = ""
//
//    // Polyline ile rota çizimi için değişkenler
//    private var polyline: Polyline? = null
//    private val polylinePoints = mutableListOf<LatLng>()
//    private val polylineOptions = PolylineOptions().color(android.graphics.Color.BLUE).width(5f)
//
//    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//        if (isGranted) {
//            startLocationUpdates()
//            getLastKnownLocation()
//        } else {
//            // Kullanıcı izin vermediğinde yapılacak işlemler
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    @SuppressLint("MissingPermission")
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentTrackRunningBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    @SuppressLint("MissingPermission")
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//        if (isAdded && _binding != null) {
//            binding.totalDistance.text = "0 km"
//            binding.averagePace.text = "0"
//            binding.energyConsump.text = "0"
//            binding.totalStepNum.text = "0"
//
//            binding.buttonToggleStart.setOnClickListener {
//                binding.tvTimer.base = SystemClock.elapsedRealtime() + stopTimer
//                binding.tvTimer.start()
//                binding.buttonToggleStart.visibility = View.GONE
//                binding.btnFinishRun.visibility = View.VISIBLE
//                binding.btnToggleStop.visibility = View.VISIBLE
//            }
//
//            binding.btnToggleStop.setOnClickListener {
//                stopTimer = binding.tvTimer.base - SystemClock.elapsedRealtime()
//                binding.tvTimer.stop()
//                binding.buttonToggleStart.visibility = View.VISIBLE
//                binding.btnFinishRun.visibility = View.GONE
//                binding.btnToggleStop.visibility = View.GONE
//            }
//
//            binding.btnFinishRun.setOnClickListener {
//                // Create bundle with data
//                val bundle = Bundle().apply {
//                    putString("activityType", "Running Activity")
//                    putString("roadTravelled", formattedDistance)
//                    putString("timeElapsed", elapsedSecond.toString())
//                    putString("caloriesBurned", formattedCalories)
//                }
//                val navOptions = NavOptions.Builder()
//                    .setPopUpTo(
//                        R.id.trackRunningFragment,
//                        true
//                    )
//                    .build()
//                findNavController().navigate(R.id.action_trackRunningFragment_to_addExerciseFragment, bundle,navOptions)
//            }
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        myMap = googleMap
//        myMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
//
//        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        locationListener = object : LocationListener {
//            override fun onProviderDisabled(provider: String) {
//                if (provider == LocationManager.GPS_PROVIDER) {
//                    showEnableGPSDialog()
//                }
//            }
//
//            private fun showEnableGPSDialog() {
//                val builder = AlertDialog.Builder(requireContext())
//                builder.setTitle("GPS is Disabled")
//                builder.setMessage("Please enable GPS to use this feature.")
//                builder.setPositiveButton("Enable GPS") { _, _ ->
//                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                }
//                builder.setNegativeButton("Cancel") { dialog, _ ->
//                    dialog.dismiss()
//                }
//                val dialog = builder.create()
//                dialog.show()
//            }
//
//            @SuppressLint("MissingPermission")
//            override fun onLocationChanged(p0: Location) {
//                val decimalFormat = DecimalFormat("#.###")
//
//                currentLocation = LatLng(p0.latitude, p0.longitude)
////                currentLocation?.let {
////                    MarkerOptions().position(it).title("Current Location")
////                }?.let {
////                    myMap?.addMarker(it)
////                }
//                currentLocation?.let {
//                    CameraUpdateFactory.newLatLngZoom(it, 17f)
//                }?.let { myMap?.moveCamera(it) }
//
//                val dist: Double = if (prevLocation != null) {
//                    calculateDistance()
//                } else {
//                    prevLocation = currentLocation
//                    0.0
//                }
//
//                totalDistance += dist
//                totalSteps += (totalDistance / 1000).toInt()
//                formattedDistance = decimalFormat.format(totalDistance)
//                elapsedSecond = ((SystemClock.elapsedRealtime() - binding.tvTimer.base) / 1000).toInt()
//                totalEnergyConsumption += calculateCaloriesBurned(totalSteps, elapsedSecond, 25)
//                formattedCalories = decimalFormat.format(totalEnergyConsumption)
//                averageSpeed = calculateAverageSpeed(totalSteps, elapsedSecond)
//
//                binding.totalDistance.text = "$formattedDistance km"
//                binding.averagePace.text = "$averageSpeed"
//                binding.energyConsump.text = formattedCalories
//                binding.totalStepNum.text = totalSteps.toString()
//
//                // Rota çizimi
//                if (prevLocation != null && currentLocation != null) {
//                    polylinePoints.add(prevLocation!!)
//                    polylinePoints.add(currentLocation!!)
//                    if (polyline == null) {
//                        polylineOptions.addAll(polylinePoints)
//                        polyline = myMap?.addPolyline(polylineOptions)
//                    } else {
//                        polyline?.points = polylinePoints
//                    }
//                }
//
//                prevLocation = currentLocation
//            }
//        }
//
//        when {
//            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
//                startLocationUpdates()
//                getLastKnownLocation()
//            }
//            else -> {
//                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun startLocationUpdates() {
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 1f, locationListener)
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun getLastKnownLocation() {
//        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        lastKnownLocation?.let {
//            currentLocation = LatLng(it.latitude, it.longitude)
//            myMap?.addMarker(MarkerOptions().position(currentLocation!!).title("Current Location"))
//            myMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 17f))
//        }
//    }
//
//    private fun calculateAverageSpeed(stepCount: Int, elapsedTime: Int): Double {
//        val averageStepLength = 0.8
//        return (stepCount.toDouble() * averageStepLength) / (elapsedTime.toDouble() / 60)
//    }
//
//    private fun calculateCaloriesBurned(stepCount: Int, elapsedTime: Int, age: Int): Double {
//        val averageStepLength = 0.8
//        val walkingSpeed = (stepCount.toDouble() * averageStepLength) / (elapsedTime.toDouble())
//        val caloriesPerMinute = calculateCaloriesPerMinute(walkingSpeed, age)
//        return caloriesPerMinute * (elapsedTime.toDouble())
//    }
//
//    private fun calculateCaloriesPerMinute(walkingSpeed: Double, age: Int): Double {
//        val MET = calculateMET(walkingSpeed)
//        val basalMetabolicRate = calculateBasalMetabolicRate(age)
//        return MET * basalMetabolicRate / 24 / 60
//    }
//
//    private fun calculateMET(walkingSpeed: Double): Double {
//        return when {
//            walkingSpeed < 3.2 -> 2.5
//            walkingSpeed < 4.8 -> 3.0
//            walkingSpeed < 6.4 -> 3.5
//            walkingSpeed < 8.0 -> 4.3
//            else -> 5.0
//        }
//    }
//
//    private fun calculateBasalMetabolicRate(age: Int): Double {
//        return when {
//            age < 3 -> 1000.0
//            age < 10 -> 22.7 * age + 495.0
//            age < 18 -> 17.5 * age + 651.0
//            age < 30 -> 15.3 * age + 679.0
//            age < 60 -> 11.6 * age + 879.0
//            else -> 13.5 * age + 487.0
//        }
//    }
//
//    private fun calculateDistance(): Double {
//        val lastLat = prevLocation?.latitude ?: 0.0
//        val lastLong = prevLocation?.longitude ?: 0.0
//        val curLat = currentLocation?.latitude ?: 0.0
//        val curLong = currentLocation?.longitude ?: 0.0
//        return calculateDistance(curLat, curLong, lastLat, lastLong)
//    }
//
//    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//        val earthRadius = 6371f
//        val dLat = Math.toRadians(lat2 - lat1)
//        val dLon = Math.toRadians(lon2 - lon1)
//        val a = sin(dLat / 2) * sin(dLat / 2) +
//                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
//                sin(dLon / 2) * sin(dLon / 2)
//        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
//        return earthRadius * c
//    }
//
//
//}
package com.example.personalhealthtracker.feature.startnewactivity.running

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
import android.os.SystemClock
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentTrackRunningBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.text.DecimalFormat

class TrackRunningFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTrackRunningBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrackRunningViewModel by viewModels()

    private var myMap: GoogleMap? = null
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startLocationUpdates()
            getLastKnownLocation()
        } else {
            // Kullanıcı izin vermediğinde yapılacak işlemler
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackRunningBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.totalDistance.text = "0 km"
        binding.averagePace.text = "0"
        binding.energyConsump.text = "0"
        binding.totalStepNum.text = "0"

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.buttonToggleStart.setOnClickListener {
            viewModel.startTimer()
            binding.buttonToggleStart.visibility = View.GONE
            binding.btnFinishRun.visibility = View.VISIBLE
            binding.btnToggleStop.visibility = View.VISIBLE
        }

        binding.btnToggleStop.setOnClickListener {
            viewModel.stopTimer()
            binding.buttonToggleStart.visibility = View.VISIBLE
            binding.btnFinishRun.visibility = View.GONE
            binding.btnToggleStop.visibility = View.GONE
        }

        binding.btnFinishRun.setOnClickListener {
            val bundle = Bundle().apply {
                putString("activityType", "Running Activity")
                putString("roadTravelled", viewModel.formattedDistance)
                putString("timeElapsed", viewModel.elapsedSecond.toString())
                putString("caloriesBurned", viewModel.formattedCalories)
            }

            findNavController().navigate(R.id.action_trackRunningFragment_to_addExerciseFragment, bundle)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.totalDistance.text = "${state.formattedDistance} km"
            binding.averagePace.text = state.averageSpeed
            binding.energyConsump.text = state.formattedCalories
            binding.totalStepNum.text = state.totalSteps.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        myMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onProviderDisabled(provider: String) {
                if (provider == LocationManager.GPS_PROVIDER) {
                    showEnableGPSDialog()
                }
            }

            private fun showEnableGPSDialog() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("GPS is Disabled")
                builder.setMessage("Please enable GPS to use this feature.")
                builder.setPositiveButton("Enable GPS") { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }

            @SuppressLint("MissingPermission")
            override fun onLocationChanged(location: Location) {
                viewModel.updateLocation(LatLng(location.latitude, location.longitude))
                myMap?.apply {
                    viewModel.currentLocation?.let { CameraUpdateFactory.newLatLngZoom(it, 17f) }
                        ?.let { moveCamera(it) }
                    if (viewModel.polyline == null) {
                        viewModel.polylineOptions.addAll(viewModel.polylinePoints)
                        viewModel.polyline = addPolyline(viewModel.polylineOptions)
                    } else {
                        viewModel.polyline?.points = viewModel.polylinePoints
                    }
                }
            }
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates()
                getLastKnownLocation()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 1f, locationListener)
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocation?.let {
            viewModel.setInitialLocation(LatLng(it.latitude, it.longitude))
            myMap?.apply {
                viewModel.currentLocation?.let { it1 -> MarkerOptions().position(it1).title("Current Location") }
                    ?.let { it2 -> addMarker(it2) }
                viewModel.currentLocation?.let { it1 ->
                    CameraUpdateFactory.newLatLngZoom(
                        it1, 17f)
                }?.let { it2 -> moveCamera(it2) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
