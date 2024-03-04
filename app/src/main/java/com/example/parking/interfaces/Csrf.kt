package com.example.parking.interfaces

data class Csrf(
    val token: String,
    val headerName: String,
    val parameterName: String
)
