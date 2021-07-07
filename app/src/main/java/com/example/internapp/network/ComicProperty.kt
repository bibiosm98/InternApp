package com.example.internapp.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class ComicProperty(
    var data: ComicData
)

data class ComicData(
    var limit: Int,
    var total: Int,
    var count: Int,
    var results: List<Comic>
)

@Parcelize
data class Comic(
    var id: Int,
    var title: String,
    var description: String?,
    var thumbnail: Thumbnail,
    var creators: Creators,
    var urls: List<Urls>
) : Parcelable

@Parcelize
data class Urls(
    var url: String
) : Parcelable

@Parcelize
data class Thumbnail(
    var path: String,
    var extension: String
) : Parcelable

@Parcelize
data class Creators(
    var available: Int,
    var items: List<Creator>
) : Parcelable

@Parcelize
data class Creator(
    var name: String,
    var role: String
) : Parcelable
