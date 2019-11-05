package com.rcaetano.marvelheroes.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rcaetano.marvelheroes.apiCall
import com.rcaetano.marvelheroes.data.Repository
import com.rcaetano.marvelheroes.data.model.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val _screenState = MutableLiveData<ScreenState>()

    val screenState: LiveData<ScreenState> = _screenState

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun fetchCharacters() {
        ioScope.launch {

            val quantitative = apiCall {
                repository.listCharacters()
            }
        }
    }
}
