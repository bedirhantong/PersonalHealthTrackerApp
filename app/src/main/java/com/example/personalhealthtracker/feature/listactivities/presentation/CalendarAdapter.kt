package com.example.personalhealthtracker.feature.listactivities.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalhealthtracker.databinding.RowCalendarDateBinding

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class CalendarAdapter(
    private val onDaySelected: (CalendarModel) -> Unit
) : ListAdapter<CalendarModel, CalendarAdapter.CalendarViewHolder>(DIFF_CALLBACK) {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = RowCalendarDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val calendarModel = getItem(position)
        holder.bind(calendarModel, position)
    }

    inner class CalendarViewHolder(private val binding: RowCalendarDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(calendarModel: CalendarModel, position: Int) {
            binding.tvCalendarDate.text = calendarModel.date
            binding.tvCalendarDay.text = calendarModel.day

            binding.root.isSelected = (selectedPosition == position)

            binding.root.setOnClickListener {
                val oldPosition = selectedPosition
                selectedPosition = position

                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)

                onDaySelected(calendarModel)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CalendarModel>() {
            override fun areItemsTheSame(oldItem: CalendarModel, newItem: CalendarModel): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: CalendarModel, newItem: CalendarModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}



data class CalendarModel(val date: String, val day: String)