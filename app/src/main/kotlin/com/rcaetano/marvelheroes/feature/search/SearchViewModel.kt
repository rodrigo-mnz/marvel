package com.rcaetano.marvelheroes.feature.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rcaetano.marvelheroes.apiCall
import com.rcaetano.marvelheroes.data.Repository
import com.rcaetano.marvelheroes.data.model.Character
import com.rcaetano.marvelheroes.data.model.Response.Error
import com.rcaetano.marvelheroes.data.model.Response.Success
import com.rcaetano.marvelheroes.data.model.ScreenState
import com.rcaetano.marvelheroes.data.model.ScreenState.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val _screenState = MutableLiveData<ScreenState>()
    private val _characterList = MutableLiveData<ArrayList<Character>>(ArrayList())
    private val _shouldLoadMore = MutableLiveData<Boolean>(false)
    private var searchTerm = ""

    val screenState: LiveData<ScreenState> = _screenState
    val characterList: LiveData<ArrayList<Character>> = _characterList
    val shouldLoadMore: LiveData<Boolean> = _shouldLoadMore

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun searchCharacters(searchTerm: String) {
        this.searchTerm = searchTerm

        ioScope.launch {

            _screenState.postValue(LOADING)

            val response = apiCall {
                repository.searchCharacterByName(0, searchTerm)
            }

            when (response) {
                is Success -> {
                    _shouldLoadMore.postValue(response.data.data.results.size < response.data.data.total)
                    _characterList.postValue(response.data.data.results)
                    _screenState.postValue(SUCCESS)
                }
                is Error -> _screenState.postValue(ERROR)
            }
        }
    }

    fun loadNextPage() {
        ioScope.launch {

            _characterList.value?.let {
                val response = apiCall {
                    repository.searchCharacterByName(it.size, searchTerm)
                }

                when (response) {
                    is Success -> {
                        val list = ArrayList<Character>(it)
                        list.addAll(response.data.data.results)
                        _shouldLoadMore.postValue(list.size < response.data.data.total)
                        _characterList.postValue(list)
                    }
                    is Error -> _screenState.postValue(ERROR)
                }
            }
        }
    }
}
