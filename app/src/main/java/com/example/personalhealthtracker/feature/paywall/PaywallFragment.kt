package com.example.personalhealthtracker.feature.paywall

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentPaywallBinding

class PaywallFragment : Fragment() {

    private var _binding: FragmentPaywallBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val scrollSpeed = 7

    private lateinit var adapter: SubscriptionAdapter
    private var subscriptionPlans = listOf(
        SubscriptionPlan(
            title = "Weekly Plan",
            price = "$9.99/week",
            features = "✓ Daily Health Tracking\n✓ Basic Reports",
            isSelected = false
        ),
        SubscriptionPlan(
            title = "Monthly Plan",
            price = "$19.99/month",
            features = "✓ Advanced Tracking\n✓ Customized Goals",
            isSelected = false
        ),
        SubscriptionPlan(
            title = "Yearly Plan",
            price = "$99.99/year",
            features = "✓ Advanced Tracking\n✓ 24/7 Support\n✓ Customized Goals",
            isSelected = false
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaywallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAutoScrollingImages()
        setupSubscribeButton()
    }

    private fun setupAutoScrollingImages() {
        val imageList = listOf(
            R.drawable.hiker_walk_svgrepo_com,
            R.drawable.woman_running_light_skin_tone_svgrepo_com,
            R.drawable.icon_diving,
            R.drawable.scuba_diver_svgrepo_com,
            R.drawable.walking_svgrepo_com,
            R.drawable.ic_directions_walk_24
        )
        setupAutoScrollingRecyclerView(binding.recyclerTop, imageList, scrollRight = true)
        setupAutoScrollingRecyclerView(binding.recyclerMiddle, imageList, scrollRight = false, reverseLayout = true)
        setupAutoScrollingRecyclerView(binding.recyclerBottom, imageList, scrollRight = true)

        setupRecyclerView()
    }


    private fun setupSubscribeButton() {
        binding.subscribeButton.setOnClickListener {
            findNavController().navigate(R.id.action_paywallFragment_to_mainScreenFragment)
        }
    }


    private fun setupRecyclerView() {
        adapter = SubscriptionAdapter(subscriptionPlans) { selectedPlan ->
            updateSelectedPlan(selectedPlan)
        }

        binding.subscriptionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.subscriptionRecyclerView.adapter = adapter

    }

    private fun updateSelectedPlan(selectedPlan: SubscriptionPlan) {
        subscriptionPlans = subscriptionPlans.map { plan ->
            plan.copy(isSelected = plan == selectedPlan)
        }
        adapter.notifyDataSetChanged()
    }


    private fun setupAutoScrollingRecyclerView(
        recyclerView: RecyclerView,
        imageList: List<Int>,
        scrollRight: Boolean,
        reverseLayout: Boolean = false
    ) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, reverseLayout)
            adapter = GridAdapter(imageList)
            scrollToPosition(Int.MAX_VALUE / 2)
        }
        startAutoScroll(recyclerView, scrollRight)
    }

    private fun startAutoScroll(recyclerView: RecyclerView, scrollRight: Boolean) {
        val runnable = object : Runnable {
            override fun run() {
                recyclerView.smoothScrollBy(if (scrollRight) scrollSpeed else -scrollSpeed, 0)
                handler.postDelayed(this, 16)
            }
        }
        handler.post(runnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacksAndMessages(null)
    }
}
