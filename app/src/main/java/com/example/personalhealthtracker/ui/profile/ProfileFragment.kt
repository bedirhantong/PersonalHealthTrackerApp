package com.example.personalhealthtracker.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.personalhealthtracker.databinding.FragmentProfileBinding
import com.example.personalhealthtracker.other.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog





class ProfileFragment : Fragment(),EasyPermissions.PermissionCallbacks {

    private var _binding : FragmentProfileBinding?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        requestLocationPermission()


        binding.apply {
            barChartView.animation.duration = animationDuration
            barChartView.animate(barSetHorizontal)
            barChartView.onDataPointTouchListener = { index, _, _ ->
                statisticsOfChartElements.text =
                    barSetHorizontal.toList()[index]
                        .second
                        .toString() +" minute"
            }
        }


        return binding.root
    }


    companion object {
        private val barSetHorizontal = listOf(
            "MON" to 27f,
            "TUE" to 49f,
            "WED" to 6f,
            "THU" to 34f,
            "FRI" to 46f,
            "SAT" to 32f,
            "SUN" to 22f
        )
        private const val animationDuration = 500L
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    fun requestLocationPermission(){
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
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            SettingsDialog.Builder(requireActivity()).build().show()
        }else{
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
//        Toast.makeText(
//            requireContext(),
//            "Permission Granted",
//            Toast.LENGTH_SHORT
//        ).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}