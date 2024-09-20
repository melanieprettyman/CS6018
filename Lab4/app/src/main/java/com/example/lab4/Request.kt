package com.example.lab4

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//model the response expected to receive
data class JokeResponse(
    val value: String,
    val icon_url: String
)

interface JokeApiService {
    // make a GET request to the /random endpoint of the Chuck Norris API
    @GET("random")
    suspend fun getRandomJoke(): JokeResponse
}

object JokeApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.chucknorris.io/jokes/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitService: JokeApiService by lazy {
        retrofit.create(JokeApiService::class.java)
    }
}