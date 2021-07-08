package com.example.internapp.repository

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://gateway.marvel.com/"

class MarvelApiRepository {
    private val builder = OkHttpClient.Builder()
        .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
    private val client = builder.build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client)
        .build()

    private val service = retrofit.create(MarvelApiService::class.java)

    suspend fun getData(): List<Comic>? {
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
}
