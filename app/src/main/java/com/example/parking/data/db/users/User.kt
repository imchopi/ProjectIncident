package com.example.parking.data.db.users

data class User(
    val id: Int? = 2,
    val nickname: String,
    val name: String,
    val surname1: String,
    val surname2: String,
    val email: String,
    val password: String,
    val rol: String,
)
