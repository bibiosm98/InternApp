package com.example.internapp.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.internapp.MainViewModel
import com.example.internapp.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: ComicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        adapter = ComicAdapter(this, viewModel)
        binding.comicRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateObserver()
    }

    fun initStateObserver(){
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            //when()
        })
    }

}