package com.example.internapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.internapp.MainViewModel
import com.example.internapp.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: ComicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        adapter = ComicAdapter(this, viewModel)
        binding.comicRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateObserver()
    }

    fun initStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            //when()  //how to serve this? change placeholders? repeat request?
        })
    }

    fun navigateToDetailFragment(position: Int) {
        this.findNavController()
            .navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(position))
    }

}