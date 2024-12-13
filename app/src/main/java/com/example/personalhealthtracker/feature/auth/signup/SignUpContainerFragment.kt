package com.example.personalhealthtracker.feature.auth.signup

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentSignUpContainerBinding
import timber.log.Timber

class SignUpContainerFragment : Fragment() {

    private var _binding: FragmentSignUpContainerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignUpContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupButtons()
        observeViewModel()
        updateTimeline(0)
    }

    private fun setupViewPager() {
        val adapter = SignUpPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = true

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateButtonsVisibility(position)
                updateTimeline(position)
            }
        })
    }

    private fun setupButtons() {
        binding.buttonNext.setOnClickListener {
            if (binding.viewPager.currentItem < 2) {
                binding.viewPager.currentItem++
            } else {
                viewModel.signUp()
            }
        }

        binding.buttonPrevious.setOnClickListener {
            if (binding.viewPager.currentItem > 0) {
                binding.viewPager.currentItem--
            }
        }
    }

    private fun updateButtonsVisibility(position: Int) {
        binding.buttonPrevious.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
        binding.buttonNext.text = if (position == 2) getString(R.string.sign_up) else getString(R.string.next)
    }

    private fun updateTimeline(position: Int) {
        val indicators = listOf(
            binding.timeline.step1Indicator,
            binding.timeline.step2Indicator,
            binding.timeline.step3Indicator
        )

        indicators.forEachIndexed { index, indicator ->
            if (index <= position) {
                animateIndicator(indicator, false)
            } else {
                animateIndicator(indicator, true)
            }
        }
    }

    private fun animateIndicator(view: View, toInactive: Boolean) {
        val colorFrom = ContextCompat.getColor(
            requireContext(),
            if (toInactive) R.color.white else R.color.white_30
        )
        val colorTo = ContextCompat.getColor(
            requireContext(),
            if (toInactive) R.color.white_30 else R.color.white
        )

        ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo).apply {
            duration = 300
            addUpdateListener { animator ->
                view.setBackgroundColor(animator.animatedValue as Int)
            }
            start()
        }
    }

    private fun observeViewModel() {
        viewModel.signUpResult.observe(viewLifecycleOwner) { result ->
            Timber.tag("SignUpContainerFragment").d("Result: $result")
            when (result) {
                is SignUpResult.Success -> {
                    Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signUpContainerFragment_to_loginFragment)
                }
                is SignUpResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                    Log.d("SignUpContainerFragment", "Error: ${result.message}")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

