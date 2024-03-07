package com.example.parking.data.db.incidents

import com.example.parking.data.api.UserLogin
import okhttp3.Credentials
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncidentHolder @Inject constructor(){

    private var _credential: String = ""

        val credential : String
            get() {
            return _credential
        }

    private var _credentialUser: UserLogin? = null

    val credentialUser : UserLogin?
        get() {
            return _credentialUser
        }

    fun setCredential(credenciales: String) {
        _credential = credenciales
    }

    fun setCredentialUser(credenciales: UserLogin) {
        _credentialUser = credenciales
    }
}