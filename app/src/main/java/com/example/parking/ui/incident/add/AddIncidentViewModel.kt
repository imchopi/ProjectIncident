package com.example.parking.ui.incident.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// ViewModel class for adding incident functionality
@HiltViewModel
class AddIncidentViewModel @Inject constructor() : ViewModel() {

    // Mutable state flow to hold the URI of the selected photo
    private val _photoUri = MutableStateFlow<Uri?>(null)

    // Exposed immutable state flow of the URI of the selected photo
    val photoUri: StateFlow<Uri?>
        get() = _photoUri
}

