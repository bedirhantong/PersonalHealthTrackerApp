package com.example.personalhealthtracker.feature.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentSignUpStep3Binding
import java.text.SimpleDateFormat
import java.util.*

class SignUpStep3Fragment : Fragment() {

    private var _binding: FragmentSignUpStep3Binding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignUpStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGenderDropdown()
        setupDatePicker()

        binding.dateOfBirthEditText.setText(viewModel.dateOfBirth.value)
        binding.genderAutoCompleteTextView.setText(viewModel.gender.value, false)

        binding.dateOfBirthEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.setDateOfBirth(binding.dateOfBirthEditText.text.toString())
            }
        }

        binding.genderAutoCompleteTextView.setOnItemClickListener { _, _, _, _ ->
            viewModel.setGender(binding.genderAutoCompleteTextView.text.toString())
        }
    }

    private fun setupGenderDropdown() {
        val genders = arrayOf("Male", "Female", "Other", "Prefer not to say")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, genders)
        binding.genderAutoCompleteTextView.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        binding.dateOfBirthEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = android.app.DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.dateOfBirthEditText.setText(dateFormat.format(selectedDate.time))
                    viewModel.setDateOfBirth(dateFormat.format(selectedDate.time))
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

