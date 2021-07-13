package com.example.internapp.authentication

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.internapp.R
import com.example.internapp.databinding.LoginFragmentBinding
import com.example.internapp.repository.UIState
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var binding: LoginFragmentBinding
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().actionBar?.setHomeAsUpIndicator(0)
        binding = LoginFragmentBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {
            authenticateUser()
        }
        binding.btnToRegistration.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }
        initStateObserver()
        return binding.root
    }

    private fun authenticateUser() {
        val email = binding.tietLogin.text?.trim().toString()
        val password = binding.tietPassword.text?.trim().toString()

        viewModel.authenticateUser(email, password)
    }

    override fun onStart() {
        super.onStart()
        viewModel.isCurrentUser()
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