package com.example.personalhealthtracker.ui.startNewActivity

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentStartNewActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import pub.devrel.easypermissions.EasyPermissions

class StartNewActivityFragment : Fragment() {
    private var isWhite : Boolean = true
    private var chosenOne:String = ""
    private var _binding : FragmentStartNewActivityBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartNewActivityBinding.inflate(inflater,container,false)
        val view: View = binding.root

        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.GONE

        changeChosenOne()

        binding.StartButton.setOnClickListener {
            if (chosenOne == "running"){
                Navigation.findNavController(it).navigate(R.id.action_startNewActivityFragment2_to_trackRunningFragment)
            }
            else if (chosenOne == "stepCounting"){
//                Navigation.findNavController(it).navigate(R.id.action_startNewActivityFragment2_to_stepCountingFragment)
            }
            else{
                Toast.makeText(requireContext(),"Please choose running or step counting activity because that is the " +
                        "only feature exist",Toast.LENGTH_LONG).show()
            }
        }

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_startNewActivityFragment2_to_profileFragment2)
            Toast.makeText(requireContext(),"You canceled the starting the new activity",Toast.LENGTH_SHORT).show()
        }

        return view
    }


    override fun onResume() {
        super.onResume()

        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.GONE

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.VISIBLE
    }


    private fun changeChosenOne(){
        binding.runningView.setOnClickListener {
            chosenOne ="running"
            if (isWhite){
                binding.runningView.setBackgroundColor(Color.CYAN)
                isWhite = false
            }else{
                binding.runningView.setBackgroundColor(Color.WHITE)
                isWhite = true
            }
        }
        binding.hikingView.setOnClickListener{
            chosenOne ="hiking"
        }

        binding.stepCounting.setOnClickListener{
            chosenOne ="stepCounting"
            if (isWhite){
                binding.stepCounting.setBackgroundColor(Color.CYAN)
                isWhite = false
            }else{
                binding.stepCounting.setBackgroundColor(Color.WHITE)
                isWhite = true
            }
        }

        binding.divingView.setOnClickListener{
            chosenOne ="diving"
        }
    }


}