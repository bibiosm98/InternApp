package com.example.internapp.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.internapp.R
import com.example.internapp.databinding.LoginFragmentBinding
import com.example.internapp.repository.UIState
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var binding: LoginFragmentBinding
    private var backPressCount: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {
            authenticateUser()
        }
        binding.btnToRegistration.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }
        initStateObserver()
        overrideBackButton()
        return binding.root
    }

    private fun overrideBackButton() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (backPressCount >= 1) {
                        requireActivity().finish()
                    } else {
                        backPressCount++
                        Snackbar.make(
                            requireView(),
                            resources.getString(R.string.pressAgain),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun authenticateUser() {
        val email = binding.tietLogin.text?.trim().toString()
        val password = binding.tietPassword.text?.trim().toString()

        viewModel.authenticateUser(email, password)
    }

    private fun initStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            when (viewModel.uiState.value) {
                UIState.OnSuccess -> {
                    binding.pbLogin.visibility = View.GONE
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    viewModel.navigationDone()
                }
                UIState.InProgress -> {
                    binding.pbLogin.visibility = View.VISIBLE
                }
                UIState.OnError -> {
                    binding.pbLogin.visibility = View.GONE
                    Snackbar.make(
                        requireView(),
                        resources.getString(R.string.signInError),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                else -> {
                }
            }
        })
    }
}