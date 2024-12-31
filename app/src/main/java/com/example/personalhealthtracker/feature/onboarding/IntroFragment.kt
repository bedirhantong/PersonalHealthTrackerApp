package com.example.personalhealthtracker.feature.onboarding

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private var _binding : FragmentIntroBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsContainer: LinearLayout
    private val dots = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.viewPager
        dotsContainer = binding.dotsContainer

        val onboardingItems = listOf(
            OnboardingItem(
                R.drawable.logo,
                "Track Your Health",
                "Monitor your daily activities and health metrics with ease."
            ),
            OnboardingItem(
                R.drawable.scuba_diver_svgrepo_com,
                "Personalized Insights",
                "Get tailored recommendations based on your health data."
            ),
            OnboardingItem(
                R.drawable.hiker_walk_svgrepo_com,
                "Stay Motivated",
                "Achieve your health goals with our smart tracking system."
            )
        )

        val adapter = OnboardingAdapter(onboardingItems)
        viewPager.adapter = adapter
        setupDotsIndicator(onboardingItems.size)
        setupViewPagerListener()

        binding.buttonSkip.setOnClickListener {
            navigateToLogin()
        }

        binding.buttonNext.setOnClickListener {
            if (viewPager.currentItem < onboardingItems.size - 1) {
                viewPager.currentItem += 1
            } else {
                navigateToLogin()
            }
        }
    }

    private fun setupDotsIndicator(count: Int) {
        dots.clear()
        dotsContainer.removeAllViews()

        for (i in 0 until count) {
            val dot = View(requireContext()).apply {
                val size = resources.getDimensionPixelSize(R.dimen.dot_size)
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    setMargins(8, 10, 8, 10)
                }
                background = createDotDrawable(false)
            }
            dots.add(dot)
            dotsContainer.addView(dot)
        }
        updateDots(0)
    }

    private fun setupViewPagerListener() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position)
            }
        })
    }

    private fun updateDots(position: Int) {
        dots.forEachIndexed { index, dot ->
            dot.background = createDotDrawable(index == position)
        }
    }

    private fun createDotDrawable(isSelected: Boolean): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setSize(
                resources.getDimensionPixelSize(R.dimen.dot_size),
                resources.getDimensionPixelSize(R.dimen.dot_size)
            )
            setColor(if (isSelected) Color.WHITE else Color.parseColor("#80FFFFFF"))
            cornerRadius = resources.getDimensionPixelSize(R.dimen.dot_size).toFloat()
        }
    }

    private fun navigateToLogin() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_introFragment_to_loginFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


data class OnboardingItem(
    val imageRes: Int,
    val title: String,
    val description: String
)