package com.example.personalhealthtracker

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.example.personalhealthtracker.databinding.FragmentSignupPhysicalInfoBinding

class SignupPhysicalInfo : Fragment() {


    private var _binding : FragmentSignupPhysicalInfoBinding?= null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentSignupPhysicalInfoBinding.inflate(inflater,container,false)
        progressOfAge()
        progressOfHeight()
        progressOfWeight()
        progressOfGender()


        return binding.root
    }

    fun progressOfGender(){
        val genderGroup = binding.gender
        genderGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId){
                R.id.maleButton -> {

                }
                R.id.femaleButton -> {

                }
                R.id.notToSayButton -> {

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun progressOfAge(){
        val ageSeekBar = binding.volumeSeekBar
        ageSeekBar.min = 18
        ageSeekBar.max = 100

        ageSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.ageResult.text =p1.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun progressOfHeight(){
        val heightSeekBar = binding.heightSeekBar
        heightSeekBar.min = 150
        heightSeekBar.max = 210

        heightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.heightResult.text =p1.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun progressOfWeight(){
        val weightSeekBar = binding.weightSeekBar
        weightSeekBar.min = 50
        weightSeekBar.max = 170

        weightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.weightResult.text =p1.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}