package com.example.personalhealthtracker.feature.exercisedetail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.personalhealthtracker.data.SerializableLatLng
import com.example.personalhealthtracker.databinding.FragmentExerciseDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
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
    private lateinit var mapView: MapView
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

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun animatePolyline(points: List<LatLng>, polylineOptions: PolylineOptions) {
        CoroutineScope(Dispatchers.Main).launch {
            val animatedPolyline = map?.addPolyline(polylineOptions) // Boş bir polyline başlat
            val totalPoints = points.size

            // Polyline'ı tek tek noktaları ekleyerek güncelle
            for (i in 1..totalPoints) {
                animatedPolyline?.points = points.subList(0, i)

                // Her bir noktada kamerayı yeni noktaya odakla
                val currentPoint = points[i - 1]
                map?.animateCamera(CameraUpdateFactory.newLatLng(currentPoint))

                delay(500)
            }
        }
    }



    private fun setupUI() {
        val bundle = arguments
        val polylinePointsSerializable = bundle?.getSerializable("polylinePoints") as? ArrayList<*>

        // Toplanan noktaları saklamak için bir liste
        val allPoints = mutableListOf<LatLng>()

        val polylinePoints = polylinePointsSerializable?.mapNotNull { item ->
            (item as? HashMap<*, *>)?.let { map ->
                val lat = map["latitude"] as? Double
                val lng = map["longitude"] as? Double
                if (lat != null && lng != null) {
                    val point = LatLng(lat, lng)
                    allPoints.add(point) // Noktayı toplama
                    SerializableLatLng(lat, lng)
                } else {
                    null
                }
            }
        }?.toCollection(ArrayList())

        // Gelen noktaları loglayalım
        println("Gelen polylinePoints: $polylinePoints")
        println("All points: $allPoints")

        if (polylinePoints.isNullOrEmpty()) {
            println("Polyline çizilecek nokta yok.")
            return
        }


        viewModel.setExerciseData(
            activityName = bundle.getString("activityType", "0") ?: "",
            kmTravelled = bundle.getString("roadTravelled", "0") ?: "",
            energyConsump = bundle.getString("caloriesBurned", "0") ?: "",
            elapsedTime = bundle.getString("timeElapsed", "0") ?: "",
            sourceActivity = bundle.getString("sourceActivity"),
            polylinePoints = polylinePoints
        )

        val uiState = viewModel.uiState.value
        binding.tripId.text = uiState?.activityName
        binding.tripCost.text = uiState?.energyConsump
        binding.tripDistance.text = uiState?.kmTravelled
        binding.tripDuration.text = uiState?.elapsedTime

        // Map'e polyline ekleme
        uiState?.polylinePoints?.let { points ->
            val polylineOptions = PolylineOptions().apply {
                points.forEach { add(LatLng(it.latitude, it.longitude)) }

                // Polyline'ı özelleştir
                color(Color.YELLOW)
                width(10f)
//                pattern(listOf(Dash(30f), Gap(20f)))
                geodesic(true)
                zIndex(10f)
            }



            // Polyline çizme işlemi
            val polyline = map?.addPolyline(polylineOptions)

            if (allPoints.isNotEmpty()) {
                val firstPoint = allPoints.first()
                val lastPoint = allPoints.last()

                binding.viewInvoiceButton.setOnClickListener {
                    map?.clear()
                    // İlk noktaya hedef ikonu ekleme
                    map?.addMarker(
                        MarkerOptions().position(firstPoint).title("Start").icon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                        )
                    )

                    // Son noktaya varış ikonu ekleme
                    map?.addMarker(
                        MarkerOptions().position(lastPoint).title("Destination")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                    animatePolyline(allPoints, polylineOptions)
                }
                // İlk noktaya hedef ikonu ekleme
                map?.addMarker(
                    MarkerOptions().position(firstPoint).title("Start").icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
                )

                // Son noktaya varış ikonu ekleme
                map?.addMarker(
                    MarkerOptions().position(lastPoint).title("Destination")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )

                // Hem firstPoint hem de lastPoint'i ekranda gösterecek LatLngBounds oluştur
                val boundsBuilder = LatLngBounds.Builder()
                boundsBuilder.include(firstPoint)
                boundsBuilder.include(lastPoint)

                // Eğer iki noktayı aşırı yakınlaştırmak istemiyorsanız, padding ekleyebilirsiniz
                val bounds = boundsBuilder.build()
                val padding = 260 // Piksel cinsinden padding

                // Kamera hareketini LatLngBounds kullanarak yap
                map?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.let {
            it.mapType = GoogleMap.MAP_TYPE_TERRAIN
            setupUI()
            initListeners()
        }
    }

    private fun initListeners() {

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

