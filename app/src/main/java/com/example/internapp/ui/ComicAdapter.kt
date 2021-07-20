package com.example.internapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.internapp.MainViewModel
import com.example.internapp.databinding.ComicItemBinding
import com.example.internapp.repository.Comic

class ComicAdapter(
    private val fragment: Fragment,
    private val viewModel: MainViewModel,
    private val onComicSelected: (Int) -> Unit
) :
    RecyclerView.Adapter<ComicViewHolder>() {
    private var comicList: List<Comic> = listOf()

    init {
        viewModel.comicList.observe(fragment.viewLifecycleOwner, Observer { list ->
            list?.let {
                comicList = it
                notifyDataSetChanged()
            }
        })
        viewModel.setBottomSheetState(2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val binding = ComicItemBinding.inflate(LayoutInflater.from(parent.context))
        return ComicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onComicSelected(position)
        }
        holder.bind(comicList[position])
    }

    override fun getItemCount(): Int {
        return comicList.size
    }
}