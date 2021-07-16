package com.example.internapp.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.internapp.authentication.AuthenticationViewModel
import com.example.internapp.databinding.SplashFragmentBinding
import com.example.internapp.repository.UIState


class SplashFragment : Fragment() {
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var binding: SplashFragmentBinding
    private var supportActionBar: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SplashFragmentBinding.inflate(inflater, container, false)
        initStateObserver()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            viewModel.isUserLoggedIn()
        }, 1000)
    }

    override fun onResume() {
        super.onResume()
        supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.show()
    }

    private fun initStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            when (viewModel.uiState.value) {
                UIState.OnSuccess -> {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
                    viewModel.navigationDone()
                }
                UIState.OnNotLoggedIn -> {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                    viewModel.navigationDone()
                }
                else -> {}
            }
        })
    }
}