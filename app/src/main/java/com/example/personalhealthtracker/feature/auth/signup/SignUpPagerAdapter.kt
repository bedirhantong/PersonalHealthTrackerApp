package com.example.personalhealthtracker.feature.auth.signup

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SignUpPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SignUpStep1Fragment()
            1 -> SignUpStep2Fragment()
            2 -> SignUpStep3Fragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}

