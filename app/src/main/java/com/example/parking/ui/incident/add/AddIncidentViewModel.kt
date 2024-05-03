package com.example.parking.ui.incident.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddIncidentViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> get() = _photoUri

    fun onPhotoTaken(uri: Uri) {
        // Puedes realizar aquí cualquier procesamiento adicional necesario
        _photoUri.value = uri
    }

    private val _incidentAdd = MutableStateFlow(AddIncidentUiState())
    val incidentDetail: StateFlow<AddIncidentUiState>
        get() = _incidentAdd.asStateFlow()

    fun addIncident (incident: IncidentsEntity){
        viewModelScope.launch {
            /*parkingRepository.postIncident(incident)*/
            repository.addIncident(incident)
        }
    }
}