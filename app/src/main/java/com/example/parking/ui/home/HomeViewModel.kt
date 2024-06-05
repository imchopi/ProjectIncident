package com.example.parking.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    // MutableStateFlow to hold the UI state
    private val _uiState = MutableStateFlow(HomeUiState(listOf()))

    // Expose the UI state as StateFlow to observe changes
    val uiState: StateFlow<HomeUiState>
        get() = _uiState.asStateFlow()

    // Initialize the ViewModel
    init {
        // Launch a coroutine in the viewModelScope to collect data from the repository
        viewModelScope.launch {
            // Collect incidents data from the repository
            repository.incident.collect() { incidents ->
                // Update the UI state with the collected data
                _uiState.value = HomeUiState(incidents)
            }
        }
    }

    fun addIncident(incident: IncidentsEntity) {
        viewModelScope.launch {
            try {
                repository.addIncident(incident)
            } catch (e: Exception) {
                Log.e(TAG, "Error adding incident", e)
            }
        }
    }

    fun insertAll(incidents: List<IncidentsEntity>) {
        viewModelScope.launch {
            try {
                repository.insertAll(incidents)
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting incidents", e)
            }
        }
    }
}

