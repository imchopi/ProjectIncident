package com.example.parking.ui.incident.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddIncidentViewModel : ViewModel() {
    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> get() = _photoUri

    fun onPhotoTaken(uri: Uri) {
        // Puedes realizar aquí cualquier procesamiento adicional necesario
        _photoUri.value = uri
    }

}