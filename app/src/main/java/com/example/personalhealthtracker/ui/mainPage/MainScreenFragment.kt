package com.example.personalhealthtracker.ui.mainPage

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalhealthtracker.adapter.HealthyActivityAdapter
import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.databinding.FragmentMainScreenBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainScreenFragment : Fragment() {
    private var _binding : FragmentMainScreenBinding?= null
    private val binding get() = _binding!!

    // to get instance of DB
    val db = Firebase.firestore

    // tüm aktiviteleri tutabilmek için
    var healthyActivityList = ArrayList<HealthyActivity>()

    private lateinit var recyclerViewAdapter : HealthyActivityAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun firebaseGetData(){
        db.collection("HealthyActivities").addSnapshotListener { snapshot, error ->
            if (error!=null){
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        firebaseGetData()

        // recycleView elemanları alt alta gösterilsin istiyoruz
        val layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerViewMainScreen.layoutManager = layoutManager

        recyclerViewAdapter = HealthyActivityAdapter(healthyActivityList)
        binding.recyclerViewMainScreen.adapter = recyclerViewAdapter




        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}