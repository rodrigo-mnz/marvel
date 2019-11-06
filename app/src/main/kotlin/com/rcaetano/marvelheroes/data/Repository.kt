package com.rcaetano.marvelheroes.data

import com.rcaetano.marvelheroes.data.model.CharacterListResponse

interface Repository {

    suspend fun listCharacters(offset: Int): CharacterListResponse
}
