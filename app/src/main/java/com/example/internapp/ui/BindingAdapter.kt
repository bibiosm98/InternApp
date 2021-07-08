package com.example.internapp.ui

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.internapp.R

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    var requestOptions = RequestOptions()
    requestOptions = requestOptions.transforms(CenterCrop())

    imgUrl?.let {
        val imgUri =
            it.toUri().buildUpon().scheme("https").appendPath("/portrait_uncanny.jpg").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(requestOptions)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_round_home_24)
            )
            .into(imgView)
    }
}