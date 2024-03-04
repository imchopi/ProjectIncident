package com.example.parking.data.db.incidents

import com.example.parking.data.db.users.User

data class Incident(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val user: Int
)
