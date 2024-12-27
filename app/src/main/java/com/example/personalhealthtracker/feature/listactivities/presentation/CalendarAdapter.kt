package com.example.personalhealthtracker.feature.listactivities.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.RowCalendarDateBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(private val onItemClick: (CalendarModel) -> Unit) :
    ListAdapter<CalendarModel, CalendarAdapter.CalendarViewHolder>(DiffCallback()) {

    private var selectedDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun updateSelectedDate(date: Date) {
        val newSelectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        if (selectedDate != newSelectedDate) {
            selectedDate = newSelectedDate
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = RowCalendarDateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, item.date == selectedDate)
        
        holder.itemView.setOnClickListener {
            if (selectedDate != item.date) {
                val oldDate = selectedDate
                selectedDate = item.date
                notifyDataSetChanged()
                onItemClick(item)
            }
        }
    }

    inner class CalendarViewHolder(private val binding: RowCalendarDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalendarModel, isSelected: Boolean) {
            binding.apply {
                // Gün adını ayarla (Pzt, Sal, vb.)
                tvCalendarDay.text = SimpleDateFormat("EEE", Locale.getDefault()).format(
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.date)!!
                )
                
                // Günün tarihini ayarla (1, 2, vb.)
                tvCalendarDate.text = SimpleDateFormat("d", Locale.getDefault()).format(
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.date)!!
                )

                // Seçili gün için görsel değişiklikler
                cardCalendar.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        if (isSelected) R.color.purple_500 else android.R.color.white
                    )
                )

                // Metin renklerini ayarla
                val textColor = if (isSelected) android.R.color.white else android.R.color.black
                tvCalendarDay.setTextColor(ContextCompat.getColor(root.context, textColor))
                tvCalendarDate.setTextColor(ContextCompat.getColor(root.context, textColor))

                // Seçili kartın elevation'ını artır
                cardCalendar.elevation = if (isSelected) 8f else 2f
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CalendarModel>() {
        override fun areItemsTheSame(oldItem: CalendarModel, newItem: CalendarModel) =
            oldItem.date == newItem.date

        override fun areContentsTheSame(oldItem: CalendarModel, newItem: CalendarModel) =
            oldItem == newItem
    }
}



data class CalendarModel(val date: String, val day: String)