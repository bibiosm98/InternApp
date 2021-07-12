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
import com.example.internapp.databinding.HomeFragmentBinding
import com.example.internapp.repository.UIState

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
        adapter = ComicAdapter(this, viewModel) {
            navigateToDetailFragment(it)
        }
        binding.comicRecyclerView.adapter = adapter
        refreshHome()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateObserver()
    }

    private fun initStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            when (viewModel.uiState.value) {
                UIState.InProgress -> {
                    binding.imgState.visibility = View.GONE
                    binding.progressbarHome.visibility = View.VISIBLE
                }
                UIState.OnError -> {
                    binding.imgState.visibility = View.VISIBLE
                    binding.progressbarHome.visibility = View.GONE
                    binding.imgState.setImageResource(R.drawable.ic_baseline_cloud_off_24)
                }
                UIState.OnSuccess -> {
                    binding.imgState.visibility = View.GONE
                    binding.progressbarHome.visibility = View.GONE
                }
                else -> {

                }
            }
        })
    }

    fun navigateToDetailFragment(position: Int) {
        this.findNavController()
            .navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(position))
    }

    private fun refreshHome() {
        binding.refreshHome.setOnRefreshListener {
            viewModel.getMarvelAppComics(null)
            binding.refreshHome.isRefreshing = false
        }
    }
}