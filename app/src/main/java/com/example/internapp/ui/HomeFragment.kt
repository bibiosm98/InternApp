package com.example.internapp.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.internapp.MainViewModel
import com.example.internapp.R
import com.example.internapp.databinding.HomeFragmentBinding
import com.example.internapp.repository.UIState
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: ComicAdapter
    private val fbAuth = FirebaseAuth.getInstance()

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
            R.id.logout_action -> {
                fbAuth.signOut()
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}