package com.example.personalhealthtracker.feature.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.db.williamchart.ExperimentalFeature
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.adapter.HealthyActivityAdapter
import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.databinding.FragmentProfileBinding
import com.example.personalhealthtracker.utils.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.example.personalhealthtracker.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var recyclerViewAdapter: HealthyActivityAdapter
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocationPermission()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProfile.layoutManager = layoutManager

        recyclerViewAdapter = HealthyActivityAdapter(
            onItemClick = { healthyActivity ->
                val bundle = Bundle().apply {
                    putString("activityType", healthyActivity.activityName)
                    putString("roadTravelled", healthyActivity.kmTravelled)
                    putString("timeElapsed", healthyActivity.elapsedTime)
                    putString("caloriesBurned", healthyActivity.energyConsump)
                    putSerializable("polylinePoints", ArrayList(healthyActivity.polylinePoints))
                }

                val action = ProfileFragmentDirections.actionProfileFragmentToExerciseDetailFragment().actionId
                findNavController().navigate(action,bundle)
            }
        )
        binding.recyclerViewProfile.adapter = recyclerViewAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.activities.collect { activities ->
                recyclerViewAdapter.submitList(activities)
            }
        }

        binding.apply {
//            barChartView.animation.duration = animationDuration
//            barChartView.animate(barSetHorizontal)
//            barChartView.onDataPointTouchListener = { index, _, _ ->
//                statisticsOfChartElements.text =
//                    barSetHorizontal.toList()[index].second.toString() + " minute"
//            }
        }

        binding.buttonSetting.setOnClickListener { buttonView ->
            val popupMenu = PopupMenu(requireContext(), buttonView)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.main_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.log_out -> {
                        mAuth.signOut()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        Toast.makeText(requireContext(), "You have been logged out of your account!", Toast.LENGTH_SHORT).show()
                        activity?.finish()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application cannot work without Location Permission.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
