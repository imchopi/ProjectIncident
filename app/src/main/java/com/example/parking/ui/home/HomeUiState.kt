package com.example.parking.ui.home

import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.incidents.IncidentsEntity

data class HomeUiState (
    val incident:List<Incident>,
    val errorMessage:String?=null
)