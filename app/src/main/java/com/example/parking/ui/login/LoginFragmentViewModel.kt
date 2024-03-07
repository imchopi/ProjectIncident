package com.example.parking.ui.login

import androidx.lifecycle.ViewModel
import com.example.parking.data.api.ParkingService
import com.example.parking.data.api.UserLogin
import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.incidents.IncidentHolder
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.data.db.users.UsersEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(private val service: ParkingService, private val holder: IncidentHolder): ViewModel() {
    suspend fun login(credential: String) {
        holder.setCredential(credential)
    }
    suspend fun userLogin(credential: UserLogin) {
        service.api.login(credential)
    }

    suspend fun register(user: UsersEntity): UsersEntity {
        return service.api.registerUser(user)
    }

    suspend fun addIncident(incident: IncidentsEntity): IncidentsEntity {
        return service.api.postIncidents(incident)
    }

    suspend fun getIncident(): List<Incident> {
        return service.api.getIncidents(300, 0)
    }

}