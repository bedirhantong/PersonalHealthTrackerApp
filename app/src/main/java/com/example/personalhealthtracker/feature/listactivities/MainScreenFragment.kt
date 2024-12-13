package com.example.personalhealthtracker.feature.listactivities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.adapter.HealthyActivityAdapter
import com.example.personalhealthtracker.databinding.FragmentMainScreenBinding
import com.example.personalhealthtracker.feature.listactivities.presentation.MainScreenViewModel
import com.example.personalhealthtracker.feature.profile.ProfileFragmentDirections
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var activityAdapter: HealthyActivityAdapter
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainScreenBinding.bind(view)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)

        setupActivityRecyclerView()
        observeViewModel()
        setupCalendarView()
        setInitialDate()
    }

    private fun setupActivityRecyclerView() {
        activityAdapter = HealthyActivityAdapter(
            onItemClick = { healthyActivity ->
                val bundle = Bundle().apply {
                    putString("activityType", healthyActivity.activityName)
                    putString("roadTravelled", healthyActivity.kmTravelled)
                    putString("timeElapsed", healthyActivity.elapsedTime)
                    putString("caloriesBurned", healthyActivity.energyConsump)
                    putSerializable("polylinePoints", ArrayList(healthyActivity.polylinePoints))
                }

                val action = ProfileFragmentDirections.actionProfileFragmentToExerciseDetailFragment().actionId
                findNavController().navigate(action, bundle)
            }
        )
        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewActivities.adapter = activityAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.selectedDateActivities.collect { activities ->
                activityAdapter.submitList(activities)
            }
        }
    }

    private fun setupCalendarView() {
        // MaterialDatePicker setup
        val constraintsBuilder = CalendarConstraints.Builder()
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(today)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        // Open date picker when calendar icon is clicked
        binding.calendarIcon.setOnClickListener {
            datePicker.show(childFragmentManager, datePicker.toString())
        }

        // Handle selected date
        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = Date(selection)
            viewModel.filterActivitiesByDate(selectedDate)
        }
    }

    private fun setInitialDate() {
        // Set current date as selected and filter activities for today
        val today = Date()
        viewModel.filterActivitiesByDate(today)
    }
}



