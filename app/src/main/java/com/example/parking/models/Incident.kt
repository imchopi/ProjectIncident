package com.example.parking.models

import java.util.Date

data class Incident(
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val date: Date,
    val extender_user_id: String = "",
)
