package com.example.internapp.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.internapp.databinding.RegistrationFragmentBinding

class RegistrationFragment : Fragment() {
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var binding: RegistrationFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }
}