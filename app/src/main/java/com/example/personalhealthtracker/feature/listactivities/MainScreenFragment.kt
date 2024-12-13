package com.example.personalhealthtracker.feature.listactivities

import android.os.Bundle
import android.util.Log
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
import com.example.personalhealthtracker.feature.listactivities.presentation.WeekDay
import com.example.personalhealthtracker.feature.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var weekDayAdapter: WeekDayAdapter
    private lateinit var activityAdapter: HealthyActivityAdapter
    private lateinit var viewModel: MainScreenViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainScreenBinding.bind(view)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)

        setupActivityRecyclerView()
        observeViewModel()
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
                findNavController().navigate(action,bundle)
            }
        )
        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewActivities.adapter = activityAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.activities.collect { activities ->
                activityAdapter.submitList(activities)
            }
        }

    }


    private fun generateWeekDays(): List<WeekDay> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dayFormat = SimpleDateFormat("dd", Locale.getDefault())

        return List(7) {
            val dayOfWeek = dateFormat.format(calendar.time)
            val dayOfMonth = dayFormat.format(calendar.time)
            val weekDay = WeekDay(dayOfWeek, dayOfMonth.toInt(), false)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            weekDay
        }
    }
}

