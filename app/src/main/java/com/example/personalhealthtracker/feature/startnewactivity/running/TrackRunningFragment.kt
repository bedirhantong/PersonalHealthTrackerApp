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
import androidx.navigation.fragment.findNavController
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentTrackRunningBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

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
    @SuppressLint("MissingPermission", "SetTextI18n")
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
                putString("activityType", "Running")
                putString("roadTravelled", viewModel.formattedDistance)
                putString("timeElapsed", viewModel.elapsedSecond.toString())
                putString("caloriesBurned", viewModel.formattedCalories)
                putString("totalSteps", viewModel.totalSteps.toString())
                putSerializable("polylinePoints", ArrayList(viewModel.polylinePoints))
            }
            findNavController().navigate(R.id.action_trackRunningFragment_to_addExerciseFragment, bundle)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.totalDistance.text = state.formattedDistance
            binding.averagePace.text = state.averageSpeed
            binding.energyConsump.text = state.formattedCalories
            binding.totalStepNum.text = state.totalSteps.toString()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        myMap?.mapType = GoogleMap.MAP_TYPE_HYBRID

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
                        viewModel.polylineOptions.addAll(viewModel.polylinePoints.map { LatLng(it.latitude, it.longitude) })
                        viewModel.polyline = addPolyline(viewModel.polylineOptions)
                    } else {
                        viewModel.polyline?.points = viewModel.polylinePoints.map { LatLng(it.latitude, it.longitude) }
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
