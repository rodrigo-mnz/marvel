package com.rcaetano.marvelheroes.data

import com.google.gson.JsonObject

interface Repository {

    suspend fun listCharacters(): JsonObject
}
