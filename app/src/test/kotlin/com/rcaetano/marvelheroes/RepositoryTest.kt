package com.rcaetano.marvelheroes

import com.rcaetano.marvelheroes.data.ApiService
import com.rcaetano.marvelheroes.data.RepositoryImpl
import com.rcaetano.marvelheroes.di.appModule
import com.rcaetano.marvelheroes.di.networkModule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.verify

class RepositoryTest : AutoCloseKoinTest() {

    private val apiService by inject<ApiService>()

    @Before
    fun before() {
        startKoin { modules(listOf(appModule, networkModule)) }
        declareMock<ApiService>()
    }

    @Test
    fun `when listCharacters is called, should call the apiService with the correct values`() {
        runBlocking {
            val repository = RepositoryImpl(apiService)

            repository.listCharacters(0)

            verify(apiService).listCharacters(anyString(), anyString(), eq(0), eq(20))
        }
    }

    @Test
    fun `when searchCharacterByName is called, should call the apiService with the correct values`() {
        runBlocking {
            val repository = RepositoryImpl(apiService)

            repository.searchCharacterByName(0, "TERM")

            verify(apiService).searchCharacterByName(
                anyString(),
                anyString(),
                eq(0),
                anyString(),
                eq(20)
            )
        }
    }
}
