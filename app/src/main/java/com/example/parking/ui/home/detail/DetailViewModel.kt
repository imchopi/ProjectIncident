package com.example.parking.ui.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _incidentDetail = MutableStateFlow(DetailUiState())
    val incidentDetail: StateFlow<DetailUiState>
        get() = _incidentDetail.asStateFlow()

    fun getIncident (id: Int){
        viewModelScope.launch {
            repository.getIncident(id).collect(){
                incident -> _incidentDetail.value = DetailUiState(
                 0,
                    incident.title,
                    incident.description,
                )
            }
        }
    }
}