package com.example.internapp.authentication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.internapp.R
import com.example.internapp.databinding.RegistrationFragmentBinding
import com.example.internapp.repository.UIState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var binding: RegistrationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)

        binding.btnSignUp.setOnClickListener {
            createUser()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
        viewModel.setUIState(UIState.OnWaiting)
        initStateObserver()
        return binding.root
    }

    private fun createUser() {
        val iv = InputValidator()
        val email = binding.tietLogin.text?.trim().toString()
        val password = binding.tietPassword.text?.trim().toString()
        val passwordRepeat = binding.tietPasswordRepeat.text?.trim().toString()

        try {
            iv.checkEmail(email)
            iv.checkPassword(password)
            if (password == passwordRepeat) viewModel.createUser(email, password)
            else throw Exception("Podane hasła nie są zgodne")
        } catch (e: Exception) {
            Snackbar.make(requireView(), e.message.toString(), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun initStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            when (viewModel.uiState.value) {
                UIState.OnSuccess -> {
                    binding.pbRegistration.visibility = View.GONE
                    findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment())
                    viewModel.navigationDone()
                }
                UIState.InProgress -> {
                    binding.pbRegistration.visibility = View.VISIBLE
                }
                UIState.OnWaiting -> {
                    binding.pbRegistration.visibility = View.GONE
                }
                UIState.OnError -> {
                    binding.pbRegistration.visibility = View.GONE
                    Snackbar.make(
                        requireView(),
                        resources.getString(R.string.signUpError),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                else -> {
                }
            }
        })
    }
}