package com.example.internapp.repository

import retrofit2.HttpException
import javax.inject.Inject

class MarvelApiRepository @Inject constructor(private val service: MarvelApiService) {
    suspend fun getAllData(): List<Comic>? {
        val response = service.getComics(
            "1",
            "080a502746c8a60aeab043387a56eef0",
            "6edc18ab1a954d230c1f03c590d469d2",
            null,
            null
        )
        if (response.isSuccessful) {
            return response.body()?.data?.results
        } else {
            throw HttpException(response)
        }
    }

    suspend fun getMoviesWithTitles(title: String?): List<Comic>? {
        val response = service.getComics(
            "1",
            "080a502746c8a60aeab043387a56eef0",
            "6edc18ab1a954d230c1f03c590d469d2",
            title,
            null
        )
        if (response.isSuccessful) {
            return response.body()?.data?.results
        } else {
            throw HttpException(response)
        }
    }
}
