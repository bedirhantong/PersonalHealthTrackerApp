package com.example.personalhealthtracker.feature.listactivities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.adapter.HealthyActivityAdapter
import com.example.personalhealthtracker.databinding.FragmentMainScreenBinding
import com.example.personalhealthtracker.feature.listactivities.presentation.MainScreenViewModel
import com.example.personalhealthtracker.feature.listactivities.presentation.WeekDay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainScreenViewModel by viewModels()
    private lateinit var adapter: HealthyActivityAdapter
    private lateinit var daysAdapter: WeekAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainScreenBinding.bind(view)

        setupRecyclerView()

        lifecycleScope.launch {
            viewModel.activities.collect { activities ->
                adapter.submitList(activities)
            }
        }
    }

    private fun setupRecyclerView() {
        val weekDays = getWeekDays()
        daysAdapter = WeekAdapter(weekDays) { selectedDay ->
            onDaySelected(selectedDay)
        }
        binding.recyclerViewWeek.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = daysAdapter
        }

        adapter = HealthyActivityAdapter { healthyActivity ->
            val bundle = Bundle().apply {
                putString("activityType", healthyActivity.activityName)
                putString("roadTravelled", healthyActivity.kmTravelled)
                putString("timeElapsed", healthyActivity.elapsedTime)
                putString("caloriesBurned", healthyActivity.energyConsump)
                putSerializable("polylinePoints", ArrayList(healthyActivity.polylinePoints))
            }

            val action = MainScreenFragmentDirections.actionMainScreenFragmentToExerciseDetailFragment()
            findNavController().navigate(action.actionId, bundle)
        }
        binding.recyclerViewMainScreen.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MainScreenFragment.adapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onDaySelected(selectedDay: WeekDay) {
        // Tüm günleri deselect et ve seçili günü belirgin hale getir
        daysAdapter.weekDays.forEach { it.isSelected = false }
        selectedDay.isSelected = true
        daysAdapter.notifyDataSetChanged()

        // Seçilen güne ait verileri yükle (örneğin viewModel üzerinden)
//        viewModel.loadActivitiesForDay(selectedDay)
    }

    private fun getWeekDays(): List<WeekDay> {
        val weekDays = mutableListOf<WeekDay>()
        val calendar = Calendar.getInstance()

        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        for (i in 0..6) {
            val dayOfWeek =
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            weekDays.add(WeekDay(dayOfWeek ?: "", dayOfMonth))

            calendar.add(Calendar.DAY_OF_MONTH, 1)  // Bir sonraki güne geç
        }

        return weekDays
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
