package com.example.personalhealthtracker.ui.startNewActivity

import android.content.Intent
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
import com.example.personalhealthtracker.ui.startNewActivity.breathTaking.BreathTakingExerciseActivity
import com.example.personalhealthtracker.ui.startNewActivity.stepCounting.StepCounterActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

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



        binding.runningView.setOnClickListener {
            changeChosenOne("running",binding.runningView)
        }
        binding.divingView.setOnClickListener {
            changeChosenOne("diving",binding.divingView)
        }
        binding.breathTaking.setOnClickListener {
            changeChosenOne("breathTaking",binding.breathTaking)
        }
        binding.stepCounting.setOnClickListener {
            changeChosenOne("stepCounting",binding.stepCounting)
        }

        binding.StartButton.setOnClickListener {
            if (chosenOne == "running"){
                val intent = Intent(activity, TrackRunActivity::class.java)
                startActivity(intent)
                activity?.finish()
//                Navigation.findNavController(it).navigate(R.id.action_startNewActivityFragment2_to_trackRunningFragment)
            }
            else if (chosenOne == "stepCounting"){
                val intent = Intent(activity, StepCounterActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }else if (chosenOne == "breathTaking"){
                Toast.makeText(context,"This Healthy Activity is avaliable now but you cannot save to history for now",Toast.LENGTH_LONG).show()
                val intent = Intent(activity, BreathTakingExerciseActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }else if(chosenOne == "diving"){
                Toast.makeText(context,"This Healthy Activity is not avaliable now",Toast.LENGTH_LONG).show()
//                val intent = Intent(activity, DivingActivity::class.java )
//                startActivity(intent)
//                activity?.finish()
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


    private fun changeChosenOne(text : String, view: View){
        chosenOne = text

        view.setOnClickListener {
            isWhite = if (isWhite){
                view.setBackgroundColor(Color.CYAN)

                false
            }else{
                view.setBackgroundColor(Color.WHITE)
                true
            }



        }
    }


}