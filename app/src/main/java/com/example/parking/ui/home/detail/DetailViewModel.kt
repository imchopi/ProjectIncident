package com.example.parking.ui.home.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.data.repository.Repository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private lateinit var firestore: FirebaseFirestore
    private val _incidentDetail = MutableStateFlow(DetailUiState())
    val incidentDetail: StateFlow<DetailUiState>
        get() = _incidentDetail.asStateFlow()

    /*fun getIncident (id: Int){
        viewModelScope.launch {
            repository.getIncident(id).collect(){
                incident -> _incidentDetail.value = DetailUiState(
                 0,
                    incident.title,
                    incident.description,
                )
            }
        }
    }*/
    fun getIncident (uuid: String) {
        viewModelScope.launch {
            firestore = Firebase.firestore
            firestore.collection("incidentsInfo").document(uuid).get().addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.data
                val title = data?.get("title") as? String ?: ""
                val description = data?.get("description") as? String ?: ""
                _incidentDetail.value = DetailUiState(
                    0, // Si necesitas el ID, puedes pasarlo aquí
                    title,
                    description
                )
            }.addOnFailureListener { exception ->
                // Manejar la falla en la obtención de datos, si es necesario
            }
        }
    }
}