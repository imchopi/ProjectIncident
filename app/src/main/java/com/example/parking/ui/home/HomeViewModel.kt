package com.example.parking.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.R
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
/**
 * ViewModel for the HomeFragment, managing UI-related data in a lifecycle-conscious way.
 *
 * @property repository The repository used to access data.
 */
class HomeViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    // MutableStateFlow to hold the UI state
    private val _uiState = MutableStateFlow(HomeUiState(listOf()))

    /**
     * Exposes the UI state as StateFlow to observe changes.
     *
     * @return The current UI state as a StateFlow.
     */
    val uiState: StateFlow<HomeUiState>
        get() = _uiState.asStateFlow()

    /**
     * Initializes the ViewModel.
     *
     * Launches a coroutine in the viewModelScope to collect data from the repository.
     */
    init {
        // Launch a coroutine in the viewModelScope to collect data from the repository
        viewModelScope.launch {
            // Collect incidents data from the repository
            repository.incident.collect { incidents ->
                // Update the UI state with the collected data
                _uiState.value = HomeUiState(incidents)
            }
        }
    }

    /**
     * Inserts a list of incidents into the repository.
     *
     * @param incidents The list of `IncidentsEntity` objects to be inserted.
     */
    fun insertAll(incidents: List<IncidentsEntity>) {
        viewModelScope.launch {
            try {
                repository.insertAll(incidents)
            } catch (e: Exception) {
                Log.e(TAG, e.toString(), e)
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}


