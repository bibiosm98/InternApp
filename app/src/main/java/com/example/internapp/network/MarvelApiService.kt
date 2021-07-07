package com.example.internapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApiService {
    @GET("v1/public/comics")
    suspend fun getComics(
        @Query("ts") timestamp: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("titleStartsWith") title: String?,
        @Query("limit") limit: Int?
    ): Response<ComicProperty>
}