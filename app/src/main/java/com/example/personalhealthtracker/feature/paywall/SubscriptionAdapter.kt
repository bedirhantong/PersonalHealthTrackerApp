package com.example.personalhealthtracker.feature.paywall

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalhealthtracker.databinding.ItemSubscriptionBinding
import com.example.personalhealthtracker.R

class SubscriptionAdapter(
    private val plans: List<SubscriptionPlan>,
    private val onPlanSelected: (SubscriptionPlan) -> Unit
) : RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder>() {

    inner class SubscriptionViewHolder(private val binding: ItemSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: SubscriptionPlan) {
            binding.apply {
                titleText.text = plan.title
                priceText.text = plan.price
                featuresText.text = plan.features

                root.setBackgroundResource(
                    if (plan.isSelected) {
                        R.drawable.selected_card_background
                    } else {
                        R.drawable.card_background
                    }

                )

                root.setOnClickListener {
                    onPlanSelected(plan)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val binding = ItemSubscriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SubscriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        holder.bind(plans[position])
    }

    override fun getItemCount() = plans.size
}