package com.example.personalhealthtracker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.databinding.RecyclerViewActivityBinding

class HealthyActivityAdapter(
    private val onItemClick: (HealthyActivity) -> Unit
) : ListAdapter<HealthyActivity, HealthyActivityAdapter.HealthyActivityHolder>(
    HealthyActivityDiffCallback()
) {

    inner class HealthyActivityHolder(private val binding: RecyclerViewActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(healthyActivity: HealthyActivity) {
            binding.activityType.text = healthyActivity.activityName
//            binding.tvDuration.text = healthyActivity.elapsedTime
            binding.tvRunKm.text = healthyActivity.kmTravelled+" km"
            binding.tvCalories.text = healthyActivity.energyConsump + " kcal"
            binding.root.setOnClickListener {
                onItemClick(healthyActivity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthyActivityHolder {
        val binding =
            RecyclerViewActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HealthyActivityHolder(binding)
    }

    override fun onBindViewHolder(holder: HealthyActivityHolder, position: Int) {
        val healthyActivity = getItem(position)
        holder.bind(healthyActivity)
    }
}

class HealthyActivityDiffCallback : DiffUtil.ItemCallback<HealthyActivity>() {
    override fun areItemsTheSame(oldItem: HealthyActivity, newItem: HealthyActivity): Boolean {
        return oldItem.energyConsump == newItem.energyConsump
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: HealthyActivity, newItem: HealthyActivity): Boolean {
        return oldItem == newItem
    }
}
