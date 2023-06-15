package com.example.personalhealthtracker.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentSettingsBinding
import com.example.personalhealthtracker.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {


    private var _binding : FragmentSettingsBinding?= null
    private val binding get() = _binding!!
    private lateinit var mAuth: FirebaseAuth;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        val view: View = binding.root
        mAuth = FirebaseAuth.getInstance()

        val user = mAuth.currentUser
        binding.textViewEmail.hint = user!!.email.toString()
        binding.textViewUsername.hint = user.displayName.toString()
        binding.textViewPassword.hint = "********"

        binding.buttonBack.setOnClickListener {
            if (
                binding.textViewEmail.text.toString() != user.email.toString() ||

                binding.textViewPassword.text.toString() !=  "********"
            ) {
                Toast.makeText(requireContext(),"Please save changes before going back!",Toast.LENGTH_SHORT).show()
            }else{
//                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_profileFragment2)
            }
        }

        binding.buttonLogOut.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(requireContext(),"Logged out!",Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity,MainActivity::class.java))
            activity?.finish()
        }

        binding.buttonSaveChanges.setOnClickListener {
            user.updateEmail(binding.textViewEmail.text.toString() )
            user.updatePassword(binding.textViewPassword.text.toString())
            Toast.makeText(requireContext(),"You changed your settings!",Toast.LENGTH_SHORT).show()
        }

        return view
    }

}