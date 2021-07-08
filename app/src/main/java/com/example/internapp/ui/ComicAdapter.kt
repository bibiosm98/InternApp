package com.example.internapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.internapp.MainViewModel
import com.example.internapp.databinding.ComicItemBinding
import com.example.internapp.repository.Comic
import com.google.android.material.snackbar.Snackbar

class ComicAdapter(fragment: HomeFragment, viewModel: MainViewModel) :
    RecyclerView.Adapter<ComicViewHolder>() {
    private var comicList: List<Comic> = listOf()

    init {
        viewModel.comicList.observe(fragment.viewLifecycleOwner, Observer { list ->
            list?.let {
                comicList = it
                notifyDataSetChanged()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val binding = ComicItemBinding.inflate(LayoutInflater.from(parent.context))
        return ComicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            Snackbar.make(holder.itemView, comicList[position].title, Snackbar.LENGTH_SHORT).show()
        }
        holder.bind(comicList[position])
    }

    override fun getItemCount(): Int {
        return comicList.size
    }
}

class ComicViewHolder(private val binding: ComicItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(comic: Comic) {
        var authorsList = ""
        val items = comic.creators.items
        if (items.isNotEmpty()) {
            authorsList = "written by "
            items.forEach { auth ->
                authorsList += auth.name + ", "
            }
        }
        binding.comicTitle.text = comic.title
        binding.comicAuthors.text = authorsList
        binding.comicDescription.text = comic.description

        val imgUri = comic.thumbnail.path.toUri().buildUpon().scheme("https")
            .appendPath("/portrait_uncanny.jpg").build()
        Glide.with(binding.root)
            .load(imgUri)
            .apply(RequestOptions().transform(RoundedCorners(16)))
            .into(binding.comicItemImageView)
    }
}