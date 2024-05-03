package com.example.parking.data.db.users

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UsersEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val uuid: String,
    val username: String,
    val name: String,
    val picture: String,
    val surname: String,
    val email: String,
    val password: String,
    val role: String,
)

fun List<UsersEntity>.asUser():List<User> {
    return this.map {
        User(
            it.uuid,
            it.username,
            it.name,
            it.picture,
            it.surname,
            it.email,
            it.password,
            it.role,
        )
    }
}