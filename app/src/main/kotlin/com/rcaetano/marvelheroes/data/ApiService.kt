package com.rcaetano.marvelheroes.data

import com.google.gson.JsonObject
import com.rcaetano.marvelheroes.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query
import java.sql.Timestamp

interface ApiService {

    @GET("/v1/public/characters?apikey=${BuildConfig.PUBLIC_API_KEY}")
    suspend fun listCharacters(
        @Query("hash") hash: String,
        @Query("ts") timestamp: String
    ): JsonObject
}
