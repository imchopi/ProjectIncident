package com.example.parking.data.db.incidents

data class Incident(
    var uuid: String,
    val categoryName: String = "Network",
    val title: String,
    val description: String,
    var image: String? = "",
    val date: String,
    val checked: Boolean = false,
    val resolved: Boolean = false,
    val userId: String,
)
