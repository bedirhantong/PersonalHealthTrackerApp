package com.example.personalhealthtracker

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.example.personalhealthtracker.databinding.FragmentSignupPhysicalInfoBinding
import com.google.firebase.auth.FirebaseAuth

class SignupPhysicalInfo : Fragment() {


    private var _binding : FragmentSignupPhysicalInfoBinding?= null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentSignupPhysicalInfoBinding.inflate(inflater,container,false)
        val view: View = binding.root
        mAuth = FirebaseAuth.getInstance()

        progressOfAge()
        progressOfHeight()
        progressOfWeight()
        progressOfGender()


        binding.nextButton.setOnClickListener {
            arguments?.let {
                val email = SignupPhysicalInfoArgs.fromBundle(it).email
                val password = SignupPhysicalInfoArgs.fromBundle(it).password
                val username = SignupPhysicalInfoArgs.fromBundle(it).username
                val userHeight = binding.heightResult.toString()
                val userWeight = binding.weightResult.toString()
                val userAge = binding.ageResult.toString()
                val userGender = binding.gender.toString()


                if(email == "" || password == "" || username == "" || userAge == "" || userGender == ""
                    || userWeight == "" || userHeight == ""){
                    Toast.makeText(requireContext(),"Lütfen gerekli her yeri doldurduğunuza emin olun!",Toast.LENGTH_LONG).show()
                }else{
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                // jump to login screen
                                Navigation.findNavController(requireView()).navigate(R.id.navigateTo_signupPhysicalInfo_to_loginFragment)
                            } else {
                                // give an error
                                val errorMessage = task.exception?.message
                                Toast.makeText(requireContext(), "Kayıt işlemi başarısız: $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }

        binding.prevButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateTo_signupPhysicalInfo_to_signupUserInfo)
        }



        return view
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