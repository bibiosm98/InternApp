package com.example.internapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.internapp.MainViewModel
import com.example.internapp.databinding.DetailFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: DetailFragmentBinding
    private var comicPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        comicPosition = requireArguments().getInt("comicPosition")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        viewModel.selectComic(comicPosition)

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState != 1) viewModel.setBottomSheetState(newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }
        BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_DRAGGING
        }.addBottomSheetCallback(bottomSheetCallback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFindOutMore.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(viewModel.selectedComic.value?.urls?.get(0)?.url)
            })
        }
    }

    override fun onStart() {
        super.onStart()
        BottomSheetBehavior.from(binding.bottomSheet).apply {
            viewModel.bottomSheetState.value?.let{
                state = it
            }
        }
    }
}