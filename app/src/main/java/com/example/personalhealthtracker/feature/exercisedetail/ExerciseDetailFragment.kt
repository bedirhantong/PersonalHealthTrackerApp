package com.example.personalhealthtracker.feature.exercisedetail

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.data.SerializableLatLng
import com.example.personalhealthtracker.databinding.FragmentExerciseDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ExerciseDetailFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentExerciseDetailBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null
    private val viewModel: ExerciseDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@ExerciseDetailFragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            mapType = GoogleMap.MAP_TYPE_TERRAIN
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMapToolbarEnabled = true
        }
        setupUI()
    }

    private fun setupUI() {
        val bundle = arguments ?: return
        val polylinePoints = extractPolylinePoints(bundle)

        polylinePoints?.let {
            setupViewModelData(bundle, it)
            updateUI()
            addPolylineToMap(it)
        }
    }

    private fun extractPolylinePoints(bundle: Bundle): ArrayList<SerializableLatLng>? {
        val pointsSerializable = bundle.getSerializable("polylinePoints") as? ArrayList<*>
        return pointsSerializable?.mapNotNull { item ->
            (item as? HashMap<*, *>)?.let { map ->
                val lat = map["latitude"] as? Double
                val lng = map["longitude"] as? Double
                if (lat != null && lng != null) SerializableLatLng(lat, lng) else null
            }
        }?.toCollection(ArrayList())
    }

    private fun setupViewModelData(bundle: Bundle, polylinePoints: ArrayList<SerializableLatLng>) {
        viewModel.setExerciseData(
            activityName = bundle.getString("activityType", ""),
            kmTravelled = bundle.getString("roadTravelled", ""),
            energyConsump = bundle.getString("caloriesBurned", ""),
            elapsedTime = bundle.getString("timeElapsed", ""),
            sourceActivity = bundle.getString("sourceActivity"),
            polylinePoints = polylinePoints
        )
    }

    private fun updateUI() {
        viewModel.uiState.value?.let { uiState ->
            binding.tripId.text = uiState.activityName
            binding.tripCost.text = uiState.energyConsump
            binding.tripDistance.text = uiState.kmTravelled
            binding.tripDuration.text = uiState.elapsedTime
        }
    }

    private fun addPolylineToMap(polylinePoints: ArrayList<SerializableLatLng>) {
        val polylineOptions = PolylineOptions().apply {
            color(R.color.itemResultColor)
            width(14f)
            zIndex(10f)
            addAll(polylinePoints.map { LatLng(it.latitude, it.longitude) })
        }

        val points = polylinePoints.map { LatLng(it.latitude, it.longitude) }
        map?.addPolyline(polylineOptions)
        addStartEndMarkers(points)
        moveCameraToBounds(points)
        setupPolylineAnimation(polylineOptions, points)
    }

    private fun addStartEndMarkers(points: List<LatLng>) {
        if (points.isNotEmpty()) {
            map?.apply {
                addMarker(createMarkerOptions(points.first(), "Start", R.drawable.logo))
                addMarker(createMarkerOptions(points.last(), "Destination", R.drawable.target))
            }
        }
    }

    private fun createMarkerOptions(position: LatLng, title: String, resId: Int): MarkerOptions {
        val icon = bitmapFromDrawable(resId)?.let { BitmapDescriptorFactory.fromBitmap(it) }
        return MarkerOptions().position(position).title(title).icon(icon)
    }

    private fun moveCameraToBounds(points: List<LatLng>) {
        if (points.isNotEmpty()) {
            val bounds = LatLngBounds.Builder().apply {
                include(points.first())
                include(points.last())
            }.build()
            map?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 260))
        }
    }

    private fun setupPolylineAnimation(polylineOptions: PolylineOptions, points: List<LatLng>) {
        binding.viewInvoiceButton.setOnClickListener {
            map?.clear()
            addStartEndMarkers(points)
            animatePolyline(points, polylineOptions)
        }
    }

    private fun animatePolyline(points: List<LatLng>, polylineOptions: PolylineOptions) {
        CoroutineScope(Dispatchers.Main).launch {
            val animatedPolyline = map?.addPolyline(polylineOptions)
            for (i in points.indices) {
                animatedPolyline?.points = points.subList(0, i + 1)
                map?.animateCamera(CameraUpdateFactory.newLatLng(points[i]))
                delay(500)
            }
        }
    }

    private fun bitmapFromDrawable(resId: Int): Bitmap? {
        return ResourcesCompat.getDrawable(resources, resId, null)?.run {
            Bitmap.createBitmap(90, 90, Bitmap.Config.ARGB_8888).also { canvas ->
                setBounds(0, 0, canvas.width, canvas.height)
                draw(Canvas(canvas))
            }
        }
    }

    override fun onResume() { super.onResume(); binding.mapView.onResume() }
    override fun onPause() { super.onPause(); binding.mapView.onPause() }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
    override fun onDestroy() { super.onDestroy(); binding.mapView.onDestroy() }
    override fun onLowMemory() { super.onLowMemory(); binding.mapView.onLowMemory() }
}


