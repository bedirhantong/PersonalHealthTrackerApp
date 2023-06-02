package com.example.personalhealthtracker.ui.profile

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personalhealthtracker.databinding.FragmentProfileBinding
import com.example.personalhealthtracker.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.personalhealthtracker.other.TrackingUtility
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class ProfileFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding : FragmentProfileBinding?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view: View = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
    }


    private fun requestPermissions(){
        // Checking if the user already accept the permissions
        if (TrackingUtility.hasLocationPermissions(requireContext())){
            return
        }
        // Check if the device is running on Android Q or not
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(this,
            "You need to accept the location permissions to use this app.",
            REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
                )
        }else{
            EasyPermissions.requestPermissions(this,
                "You need to accept the location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // we want to check if user perminantly denied or not

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            // if he/she perminantly denied, he wil directed to the settings of the device
            AppSettingsDialog.Builder(this).build().show()
        }else{
            requestPermissions()
        }
    }



    /*
    This function handles permissions result by default in Android
    we need to redirected to permissions result to our permissions library
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}