package com.example.personalhealthtracker.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding?= null
    private val binding get() = _binding!!
    private lateinit var mAuth: FirebaseAuth;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view: View = binding.root
        mAuth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            val email = binding.emailViewInLogin.text.toString()
            val password = binding.password1.text.toString()
//            val userC = mAuth.currentUser

//            if(userC != null){
//                startActivity(Intent(requireContext(),LoginActivity::class.java))
//                activity?.finish()
//            }

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
//            if (email == "" || password == ""){
//                Toast.makeText(requireContext(),"Lütfen email ve şifre alanlarını boş bırakmayınız!",
//                    Toast.LENGTH_LONG).show()
//            }
//            else{
////                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
////                    if (isAdded) { // Fragment hala bağlı mı kontrolü
////                        if (task.isSuccessful) {
////                            val intent = Intent(requireContext(), LoginActivity::class.java)
////                            startActivity(intent)
////                            activity?.finish()
////                        } else {
////                            val errorMessage = task.exception?.message
////                            Toast.makeText(requireContext(), "$errorMessage", Toast.LENGTH_SHORT).show()
////                        }
////                    }
////                }
//                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task->
//                    if (task.isSuccessful){
//                        val intent = Intent(requireContext(), LoginActivity::class.java)
//                        startActivity(intent)
//                        activity?.finish()
//                    }else{
//                        val errorMessage = task.exception?.message
//                        Toast.makeText(requireContext(), "$errorMessage",
//                            Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }

        }

        binding.signupButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateTo_FromLogin_ToSignup)
        }

        return view
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}