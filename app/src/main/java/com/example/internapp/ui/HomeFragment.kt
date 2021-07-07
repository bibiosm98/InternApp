package com.example.internapp.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.internapp.MainViewModel
import com.example.internapp.R
import com.example.internapp.databinding.HomeFragmentBinding
import com.example.internapp.databinding.HomeFragmentBindingImpl

class HomeFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

//        viewModel.getMarvelAppComics()
    }

}