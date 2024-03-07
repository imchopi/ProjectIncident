package com.example.parking.data.db.users

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UsersEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 2,
    val nickname: String,
    val name: String,
    val surname1: String,
    val surname2: String,
    val email: String,
    val password: String,
    val rol: String,
)

fun List<UsersEntity>.asUser():List<User> {
    return this.map {
        User(
            it.id,
            it.nickname,
            it.name,
            it.surname1,
            it.surname2,
            it.email,
            it.password,
            it.rol,
        )
    }
}