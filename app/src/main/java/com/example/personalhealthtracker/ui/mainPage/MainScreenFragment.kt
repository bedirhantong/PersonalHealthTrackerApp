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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainScreenFragment : Fragment(){
    private var _binding : FragmentMainScreenBinding?= null
    private val binding get() = _binding!!

    // to get instance of DB
    val db = Firebase.firestore

    private lateinit var mAuth: FirebaseAuth

    // tüm aktiviteleri tutabilmek için
    var healthyActivityList = ArrayList<HealthyActivity>()

    private lateinit var recyclerViewAdapter : HealthyActivityAdapter





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun firebaseGetData(){
        db.collection("HealthyActivities").whereEqualTo("userEmail",
            mAuth.currentUser?.email).orderBy("dateOfAct",
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

        binding.searchView.setOnClickListener {
            Toast.makeText(requireContext(),"This feature is currently unavaliable, wait for new updates.",
                Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}