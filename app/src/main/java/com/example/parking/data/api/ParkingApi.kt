package com.example.parking.data.api

import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.users.User
import com.example.parking.interfaces.Csrf
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

interface ParkingApi {
    @GET("/csrf")
    suspend fun getCsrf(): Csrf

    @POST("/register")
    suspend fun registerUser(@Body userData: User): User

    @GET("/api/incidents")
    suspend fun getIncidents(): List<Incident>
}


@Singleton
class ParkingService @Inject constructor(
    @Named("authHeader") authHeader: String
){

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(authHeader))
        .build()

    private val apiUrl = Retrofit.Builder()
        .baseUrl("http://192.168.1.106:8080")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ParkingApi = apiUrl.create(ParkingApi::class.java)

}

class AuthInterceptor(private val authHeader: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", authHeader)
            .build()
        return chain.proceed(modifiedRequest)
    }
}