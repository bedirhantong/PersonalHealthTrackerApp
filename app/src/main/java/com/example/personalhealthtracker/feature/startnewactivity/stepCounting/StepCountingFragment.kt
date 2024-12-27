package com.example.personalhealthtracker.feature.startnewactivity.stepCounting

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.example.personalhealthtracker.R
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import pub.devrel.easypermissions.EasyPermissions
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat

class StepCountingFragment : Fragment() {

    private val viewModel: StepCountingViewModel by viewModels()
    private val REQUEST_CODE_LOCATION_PERMISSION = 1001

    private lateinit var stepCountText: TextView
    private lateinit var heartRateText: TextView
    private lateinit var distanceText: TextView
    private lateinit var timeText: TextView
    private lateinit var startButton: Button
    private lateinit var progressBar: CircularProgressBar
    private lateinit var lottieAnimation: LottieAnimationView

    private lateinit var locationManager: LocationManager
    private var previousLocation: Location? = null

    companion object {
        fun newInstance() = StepCountingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_step_counting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stepCountText = view.findViewById(R.id.step_counting)
        heartRateText = view.findViewById(R.id.heartRateDisplay)
        distanceText = view.findViewById(R.id.distanceDisplay)
        timeText = view.findViewById(R.id.timeDisplay)
        startButton = view.findViewById(R.id.startButton)
        progressBar = view.findViewById(R.id.progressBar)
        lottieAnimation = view.findViewById(R.id.lottie_animation)

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationTracking()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs location permissions to track your steps and distance",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        viewModel.stepCount.observe(viewLifecycleOwner, Observer {
            stepCountText.text = "Steps: $it"
            progressBar.progress = it.toFloat()
        })

        viewModel.heartRate.observe(viewLifecycleOwner, Observer {
            heartRateText.text = "Heart Rate: $it bpm"
        })

        viewModel.distance.observe(viewLifecycleOwner, Observer {
            distanceText.text = "Distance: %.2f km".format(it / 1000)
        })

        viewModel.time.observe(viewLifecycleOwner, Observer {
            timeText.text = "Time: $it min"
        })

        startButton.setOnClickListener {
            if (viewModel.isTracking.value == true) {
                stopTracking()
            } else {
                startTracking()
            }
        }
    }

    private fun startLocationTracking() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 5f, object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val distance = previousLocation?.distanceTo(location) ?: 0f
                previousLocation = location

                viewModel.updateDistance(viewModel.distance.value?.plus(distance) ?: distance)
                viewModel.updateStepCount((viewModel.distance.value?.toInt() ?: 0) / 10)  // 10 meters per step as an approximation
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        })
    }

    private fun startTracking() {
        viewModel.startTracking()
        startButton.text = "Stop Tracking"
        lottieAnimation.playAnimation()
    }

    private fun stopTracking() {
        viewModel.stopTracking()
        startButton.text = "Start Tracking"
        lottieAnimation.cancelAnimation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                startLocationTracking()
            }
        }
    }
}
