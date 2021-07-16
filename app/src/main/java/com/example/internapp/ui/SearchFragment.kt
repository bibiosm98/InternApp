package com.example.internapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.internapp.MainViewModel
import com.example.internapp.R
import com.example.internapp.databinding.SearchFragmentBinding
import com.example.internapp.repository.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: SearchFragmentBinding
    private lateinit var adapter: ComicAdapter
    private lateinit var chosenTitle: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        adapter = ComicAdapter(this, viewModel) {
            navigateToDetailFragment(it)
        }
        binding.rvComicSearchView.adapter = adapter
        viewModel.clearComicList()

        binding.svComicSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                chosenTitle = query.toString()
                viewModel.getMarvelAppComicsByTitle(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return binding.root
    }

    fun navigateToDetailFragment(position: Int) {
        viewModel.navigatedFrom()
        this.findNavController()
            .navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(position))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateObserver()
    }

    private fun initStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            when (viewModel.uiState.value) {
                UIState.OnWaiting -> {
                    binding.imgState.visibility = View.VISIBLE
                    binding.tvInfo.visibility = View.VISIBLE
                    binding.pbSearch.visibility = View.GONE
                }
                UIState.InProgress -> {
                    binding.imgState.visibility = View.GONE
                    binding.tvInfo.visibility = View.GONE
                    binding.pbSearch.visibility = View.VISIBLE
                }
                UIState.OnError -> {
                    binding.imgState.visibility = View.VISIBLE
                    binding.imgState.setImageResource(R.drawable.ic_baseline_cloud_off_24)
                    binding.pbSearch.visibility = View.GONE
                }
                UIState.OnSuccess -> {
                    binding.imgState.visibility = View.GONE
                    binding.tvInfo.visibility = View.GONE
                    binding.pbSearch.visibility = View.GONE
                    if (viewModel.comicList.value?.isEmpty() == true) {
                        binding.tvInfo.visibility = View.VISIBLE
                        binding.tvInfo.text =
                            resources.getString(R.string.search_error, chosenTitle)
                    }
                }
                else -> {
                }
            }
        })
    }
}