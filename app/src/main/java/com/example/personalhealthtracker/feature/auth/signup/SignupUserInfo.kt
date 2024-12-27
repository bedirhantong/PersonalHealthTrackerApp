import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentSignupUserInfoBinding
import com.example.personalhealthtracker.feature.auth.signup.SignUpViewModel

class SignUpUserInfoFragment : Fragment() {

    private var _binding: FragmentSignupUserInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignUpViewModel // Assuming you have a SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        binding.signUpButton.setOnClickListener {
            viewModel.setUsername(binding.usernameEditText.text.toString().trim())
            viewModel.setEmail(binding.emailEditText.text.toString().trim())
            viewModel.setPassword(binding.passwordEditText.text.toString().trim())
            viewModel.setFullName(binding.fullNameEditText.text.toString().trim())
            viewModel.setDateOfBirth(binding.dateOfBirthEditText.text.toString().trim())
            viewModel.setGender(binding.genderAutoCompleteTextView.text.toString().trim())
            viewModel.signUp()
        }

        binding.loginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_mainScreenFragment)
        }

        binding.usernameEditText.setText(viewModel.username.value)
        binding.emailEditText.setText(viewModel.email.value)
        binding.passwordEditText.setText(viewModel.password.value)
        binding.fullNameEditText.setText(viewModel.fullName.value)
        binding.dateOfBirthEditText.setText(viewModel.dateOfBirth.value)
        binding.genderAutoCompleteTextView.setText(viewModel.gender.value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

