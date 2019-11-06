package com.rcaetano.marvelheroes.data

import com.rcaetano.marvelheroes.BuildConfig
import com.rcaetano.marvelheroes.data.model.CharacterListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v1/public/characters?apikey=${BuildConfig.PUBLIC_API_KEY}")
    suspend fun listCharacters(
        @Query("hash") hash: String,
        @Query("ts") timestamp: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 20
    ): CharacterListResponse
}
