package com.example.internapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.internapp.MainViewModel
import com.example.internapp.databinding.SearchFragmentBinding

class SearchFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: SearchFragmentBinding
    private lateinit var adapter: ComicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
//        adapter = ComicAdapter(this, viewModel) //change HomeFragment to fragment inside Adapter.kt and refactor navigation
        return binding.root
    }
}