package com.example.personalhealthtracker.ui.startNewActivity

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

class StartNewActivityFragment : Fragment() {

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
            else{
                Toast.makeText(requireContext(),"Please choose running activity because that is the " +
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
//            binding.divingView.setBackgroundColor(resources.getColor(R.color.black))
//            if(chosenOne != "running"){
//                binding.runningView.setImageResource(R.drawable.running_icon_selected)
            chosenOne ="running"
//            }
//            else
//                binding.runningView.setImageResource(R.drawable.running_nonselected_icon)
        }

        binding.hikingView.setOnClickListener{
//            if (chosenOne != "hiking"){
//                binding.hikingView.setImageResource(R.drawable.hiking_icon_selected)
            chosenOne ="hiking"
//            }else
//                binding.hikingView.setImageResource(R.drawable.hiking_nonselected_icon)

        }

        binding.cyclingView.setOnClickListener{
//            if (chosenOne != "cycling"){
//                binding.cyclingView.setImageResource(R.drawable.cycling_icon_selected)
            chosenOne ="cycling"
//            } else
//                binding.cyclingView.setImageResource(R.drawable.cycling_nonselected_icon)
        }

        binding.divingView.setOnClickListener{
//            if (chosenOne != "diving"){
//                binding.divingView.setImageResource(R.drawable.diving_icon_selected)
            chosenOne ="diving"
//            }else
//                binding.divingView.setImageResource(R.drawable.diving_nonselected_icon)
        }
    }


}