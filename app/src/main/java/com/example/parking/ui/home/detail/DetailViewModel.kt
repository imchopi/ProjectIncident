package com.example.parking.ui.home.detail

import android.util.Log
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
    // Firestore instance to interact with Firebase Firestore
    private lateinit var firestore: FirebaseFirestore

    // MutableStateFlow to emit UI state changes
    private val _incidentDetail = MutableStateFlow(DetailUiState())

    // Exposed as StateFlow to observe UI state changes
    val incidentDetail: StateFlow<DetailUiState>
        get() = _incidentDetail.asStateFlow()

    // Function to fetch incident details from Firestore
    fun getIncident (uuid: String) {
        viewModelScope.launch {
            // Initialize Firestore instance
            firestore = Firebase.firestore

            // Fetch incident details from Firestore
            firestore.collection("incidentsInfo").document(uuid).get().addOnSuccessListener { documentSnapshot ->
                // Parse data from document snapshot
                val data = documentSnapshot.data
                val title = data?.get("title") as? String ?: ""
                val description = data?.get("description") as? String ?: ""
                val picture = data?.get("image") as? String ?: ""
                val category = data?.get("categoryName") as? String ?: ""

                // Update UI state with the fetched incident details
                _incidentDetail.value = DetailUiState(
                    0,
                    picture,
                    title,
                    description,
                    category
                )
            }.addOnFailureListener { exception ->
                // Log any errors that occur during fetching
                exception.message?.let { Log.e("Error", it) }
            }
        }
    }
}
