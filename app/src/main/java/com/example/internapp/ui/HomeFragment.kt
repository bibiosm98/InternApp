package com.example.internapp.ui

import android.os.Bundle
import android.view.*
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.internapp.MainViewModel
import com.example.internapp.R
import com.example.internapp.databinding.HomeFragmentBinding
import com.example.internapp.repository.UIState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: ComicAdapter
    private var backPressCount: Int = 0
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

        setHasOptionsMenu(true)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isComicListEmpty()
        initStateObserver()
    }

    private fun initStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            when (viewModel.uiState.value) {
                UIState.InProgress -> {
                    binding.imgState.visibility = View.GONE
                    binding.pbHome.visibility = View.VISIBLE
                }
                UIState.OnError -> {
                    binding.imgState.visibility = View.VISIBLE
                    binding.pbHome.visibility = View.GONE
                    binding.imgState.setImageResource(R.drawable.ic_baseline_cloud_off_24)
                }
                UIState.OnSuccess -> {
                    binding.imgState.visibility = View.GONE
                    binding.pbHome.visibility = View.GONE
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
            viewModel.getAllMarvelAppComics()
            binding.refreshHome.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_action -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}