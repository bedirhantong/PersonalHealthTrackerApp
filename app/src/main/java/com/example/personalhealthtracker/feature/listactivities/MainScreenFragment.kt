package com.example.personalhealthtracker.feature.listactivities

import android.app.DatePickerDialog
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
import com.example.personalhealthtracker.feature.listactivities.presentation.CalendarAdapter
import com.example.personalhealthtracker.feature.listactivities.presentation.CalendarModel
import com.example.personalhealthtracker.feature.listactivities.presentation.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var activityAdapter: HealthyActivityAdapter
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var calendarAdapter: CalendarAdapter

    private var currentCalendar: Calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainScreenBinding.bind(view)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)

        setupActivityRecyclerView()
        setupCalendarRecyclerView()
        setupDatePicker()
        observeViewModel()
        setInitialDate()
    }

    private fun setupDatePicker() {
        binding.tvDateMonth.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val year = currentCalendar.get(Calendar.YEAR)
        val month = currentCalendar.get(Calendar.MONTH)
        val day = currentCalendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                currentCalendar.set(selectedYear, selectedMonth, selectedDay)
                updateCalendarUI()
                
                val weekDays = generateWeekDays()
                calendarAdapter.updateSelectedDate(currentCalendar.time)
                calendarAdapter.submitList(weekDays)
                
                viewModel.filterActivitiesByDate(currentCalendar.time)
            },
            year,
            month,
            day
        ).show()
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

                val action = MainScreenFragmentDirections.actionMainScreenFragmentToExerciseDetailFragment().actionId
                findNavController().navigate(action, bundle)
            }
        )
        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewActivities.adapter = activityAdapter
    }

    private fun setupCalendarRecyclerView() {
        updateCalendarUI()

        calendarAdapter = CalendarAdapter { selectedDay ->
            val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedDay.date) ?: Date()
            currentCalendar.time = selectedDate
            updateCalendarUI()
            viewModel.filterActivitiesByDate(selectedDate)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = calendarAdapter

        calendarAdapter.submitList(generateWeekDays())

        binding.ivCalendarPrevious.setOnClickListener {
            changeWeek(-1)
        }

        binding.ivCalendarNext.setOnClickListener {
            changeWeek(1)
        }
    }

    private fun changeWeek(offset: Int) {
        currentCalendar.add(Calendar.WEEK_OF_YEAR, offset)
        updateCalendarUI()
        val weekDays = generateWeekDays()
        calendarAdapter.submitList(weekDays)
        calendarAdapter.updateSelectedDate(currentCalendar.time)
    }

    private fun updateCalendarUI() {
        val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.tvDateMonth.text = monthYearFormat.format(currentCalendar.time)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.selectedDateActivities.collect { activities ->
                activityAdapter.submitList(activities)
                if (viewModel.isListEmpty) {
                    binding.tvNoActivities.visibility = View.VISIBLE
                    binding.recyclerViewActivities.visibility = View.GONE
                }else {
                    binding.tvNoActivities.visibility = View.GONE
                    binding.recyclerViewActivities.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setInitialDate() {
        val today = Date()
        currentCalendar.time = today
        viewModel.filterActivitiesByDate(today)
        calendarAdapter.updateSelectedDate(today)
    }

    private fun generateWeekDays(): List<CalendarModel> {
        val calendar = currentCalendar.clone() as Calendar
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val weekDays = mutableListOf<CalendarModel>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())

        for (i in 0..6) {
            val date = calendar.time
            val day = dayFormat.format(date)
            weekDays.add(CalendarModel(dateFormat.format(date), day))
            calendar.add(Calendar.DATE, 1)
        }

        return weekDays
    }
}
