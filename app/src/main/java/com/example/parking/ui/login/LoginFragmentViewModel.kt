package com.example.parking.ui.login

import androidx.lifecycle.ViewModel
import com.example.parking.data.api.UserLogin
import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.incidents.IncidentHolder
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.data.db.users.UsersEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(private val holder: IncidentHolder): ViewModel() {
    suspend fun login(credential: String) {
        holder.setCredential(credential)
    }


}