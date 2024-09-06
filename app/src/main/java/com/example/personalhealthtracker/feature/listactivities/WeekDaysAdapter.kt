package com.example.personalhealthtracker.feature.listactivities

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.feature.listactivities.presentation.WeekDay

class WeekAdapter(
    val weekDays: List<WeekDay>,
    private val onDayClick: (WeekDay) -> Unit
) : RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {

    inner class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayOfWeekText: TextView = itemView.findViewById(R.id.textDayOfWeek)
        private val dayOfMonthText: TextView = itemView.findViewById(R.id.textDayOfMonth)

        @SuppressLint("SetTextI18n")
        fun bind(weekDay: WeekDay) {
            dayOfWeekText.text = weekDay.dayOfWeek
            dayOfMonthText.text = weekDay.dayOfMonth.toString()

            if (weekDay.isSelected) {
                dayOfWeekText.setTextColor(Color.WHITE)
                dayOfMonthText.setTextColor(Color.WHITE)
                itemView.setBackgroundResource(R.drawable.startnewactivityshape)
            } else {
                dayOfWeekText.setTextColor(Color.BLACK)
                dayOfMonthText.setTextColor(Color.BLACK)
                itemView.setBackgroundResource(R.drawable.startnewactivityshape)
            }

            itemView.setOnClickListener {
                onDayClick(weekDay)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day_of_week, parent, false)
        return WeekViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        holder.bind(weekDays[position])
    }

    override fun getItemCount() = weekDays.size
}

