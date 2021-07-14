package com.example.internapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.internapp.MainViewModel
import com.example.internapp.R
import com.example.internapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvUserName.text = resources.getString(R.string.welcome, viewModel.getUserData())
        binding.ivLogout.setOnClickListener {
            viewModel.signOutUser()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
        }
    }
}