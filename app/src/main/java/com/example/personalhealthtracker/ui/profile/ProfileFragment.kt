package com.example.personalhealthtracker.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.db.williamchart.ExperimentalFeature
import com.example.personalhealthtracker.adapter.HealthyActivityAdapter
import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.databinding.FragmentProfileBinding
import com.example.personalhealthtracker.other.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog





class ProfileFragment : Fragment(),EasyPermissions.PermissionCallbacks {

    private var _binding : FragmentProfileBinding?= null
    private val binding get() = _binding!!

    val db = Firebase.firestore

    // tüm aktiviteleri tutabilmek için
    var healthyActivityList = ArrayList<HealthyActivity>()

    private lateinit var mAuth: FirebaseAuth

    private lateinit var recyclerViewAdapter : HealthyActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }


    @OptIn(ExperimentalFeature::class)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        requestLocationPermission()

        binding.headerProfile.text = "\nWelcome Back ${mAuth.currentUser?.displayName}"

        firebaseGetData()


        // recycleView elemanları alt alta gösterilsin istiyoruz
        val layoutManager = LinearLayoutManager(this.requireContext())
        binding.rcyclerViewProfile.layoutManager = layoutManager

        recyclerViewAdapter = HealthyActivityAdapter(healthyActivityList)
        binding.rcyclerViewProfile.adapter = recyclerViewAdapter




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


    @SuppressLint("NotifyDataSetChanged")
    fun firebaseGetData(){
        db.collection("HealthyActivities").whereEqualTo("userEmail",
            mAuth.currentUser?.email
        ).orderBy("dateOfAct",
            Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if (error!=null){
                println(error.localizedMessage)
                Toast.makeText(this.requireContext(),error.localizedMessage,Toast.LENGTH_SHORT).show()
            }else{
                if (snapshot != null){

                    // eğer snapshot içinde doküman yoksa?
                    if (!snapshot.isEmpty){
                        val documents = snapshot.documents
                        healthyActivityList.clear()
                        for (document in documents){
                            val actName = document.get("activityName") as String
//                            val userEmail = document.get("userEmail") as String
                            val elapsedTime = document.get("elapsedTime") as String
                            val energyConsump = document.get("energyConsump") as String
                            val kmTravelled = document.get("kmTravelled") as String
                            val imageUrl = document.get("imageUrl") as String?

                            val healthyActivity = HealthyActivity(actName, elapsedTime,kmTravelled,energyConsump,imageUrl)
                            healthyActivityList.add(healthyActivity)

                        }
                        // recyclerView adapteri yeni veri için uyarıyoruz böylece yeni verileri de ekleyecek
                        recyclerViewAdapter.notifyDataSetChanged()

                    }
                }
            }
        }
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