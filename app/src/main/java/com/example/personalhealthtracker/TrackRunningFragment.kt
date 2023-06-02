package com.example.personalhealthtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.personalhealthtracker.databinding.FragmentTrackRunningBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class TrackRunningFragment : Fragment() {
    private var _binding: FragmentTrackRunningBinding? = null
    private val binding get() = _binding!!

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

        binding.drawBackButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_trackRunningFragment_to_startNewActivityFragment2)
            Toast.makeText(requireContext(),"You canceled the activity you have just been doing", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStop() {
        super.onStop()
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.GONE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.VISIBLE


        _binding = null
    }
}