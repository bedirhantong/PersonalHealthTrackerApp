package com.example.personalhealthtracker.ui.startNewActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentTrackRunningBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomnavigation.BottomNavigationView


class TrackRunningFragment : Fragment() {
    private var _binding: FragmentTrackRunningBinding? = null
    private val binding get() = _binding!!

    private var map: GoogleMap? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrackRunningBinding.inflate(inflater,container,false)





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.GONE

        binding.mapView.onCreate(savedInstanceState)

        //this will load our map
        binding.mapView.getMapAsync {
            map = it
            /*
            If you do not worry about life cycle of mapView when you use it in a blank fragment then
            your app will crash here
             */
        }
//        binding.drawBackButton.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.action_trackRunningFragment_to_startNewActivityFragment2)
//            Toast.makeText(requireContext(),"You canceled the activity you have just been doing", Toast.LENGTH_SHORT).show()
//        }

    }



    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
//        binding.mapView?.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // this will help us to cache our map
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.VISIBLE

//        binding.mapView.onDestroy()
//        binding.mapView?.onDestroy()


        _binding = null
    }
}