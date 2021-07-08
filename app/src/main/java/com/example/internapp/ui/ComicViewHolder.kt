package com.example.internapp.ui

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.internapp.databinding.ComicItemBinding
import com.example.internapp.repository.Comic

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