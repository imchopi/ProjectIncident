package com.example.parking.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(listOf()))
    val uiState: StateFlow<HomeUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                repository.refreshList()
                Log.d("Temilla", "El tema: " + repository.refreshList().toString())
            }
            catch (e: IOException) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }

        viewModelScope.launch {
            repository.incident.collect() {


                _uiState.value = HomeUiState(it)
                Log.d("Temilla", "El tema: " + _uiState.value.incident)
            }
        }
    }
}
