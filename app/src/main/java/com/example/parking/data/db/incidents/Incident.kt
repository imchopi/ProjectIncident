package com.example.parking.data.db.incidents

data class Incident(
    val id: Int? = 2,
    val title: String,
    val description: String,
    val status: String,
    val user: Int,
    val image: String?,
    val imageType: String?
)
