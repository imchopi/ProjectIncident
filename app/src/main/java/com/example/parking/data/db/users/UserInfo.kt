package com.example.parking.data.db.users

data class UserInfo(
    var uuid: String,
    val username: String,
    val name: String,
    val picture: String,
    val surname: String,
    val email: String,
    val role: String,
)
