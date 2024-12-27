package com.example.personalhealthtracker.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.adapter.HealthyActivityAdapter
import com.example.personalhealthtracker.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var recyclerViewAdapter: HealthyActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeUserProfile()
        observeActivities()
    }

    private fun setupRecyclerView() {
        recyclerViewAdapter = HealthyActivityAdapter(
            onItemClick = { healthyActivity ->
            }
        )
        binding.recyclerViewProfile.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }
    }

    private fun observeUserProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userProfile.collect { userProfile ->
                userProfile?.let { populateUserProfile(it) }
            }
        }
    }

    private fun populateUserProfile(userProfile: Map<String, Any>) {
        binding.apply {
            userName.text = userProfile["fullName"] as String? ?: "Ad Soyad"
            userHandle.text = "@${userProfile["username"] as String? ?: "kullanici"}"
            userBio.text = userProfile["description"] as String? ?: "Bio eklenmemiş."

            // Profil Resmini Güncelle
            Glide.with(this@ProfileFragment)
                .load(userProfile["profileImageUrl"] as String?)
                .placeholder(R.drawable.card_background)
                .circleCrop()
                .into(profileImage)

            // Kapak Fotoğrafını Güncelle
            Glide.with(this@ProfileFragment)
                .load(userProfile["coverImageUrl"] as String?)
                .placeholder(R.drawable.circle)
                .centerCrop()
                .into(coverPhoto)

        }
    }

    private fun observeActivities() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.activities.collect { activities ->
                recyclerViewAdapter.submitList(activities)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

