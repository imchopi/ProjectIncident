package com.example.parking.data.api

import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.incidents.IncidentHolder
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.data.db.users.User
import com.example.parking.data.db.users.UsersEntity
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
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/*interface ParkingApi {
    @GET("/csrf")
    suspend fun getCsrf(): Csrf

    @POST("/api/login")
    suspend fun login(@Body userLogin: UserLogin): Boolean

    @POST("/register")
    suspend fun registerUser(@Body userData: UsersEntity): UsersEntity

    @GET("/api/incidents")
    suspend fun getIncidents(@Query("limit") limit:Int=20, @Query("offset") offset:Int=0): List<Incident>

    @POST("/api/incident")
    suspend fun postIncidents(@Body incidentsEntity: IncidentsEntity): IncidentsEntity
}


@Singleton
class ParkingService @Inject constructor(
    provideAuthHeader: AuthInterceptor
){

    private val client = OkHttpClient.Builder()
        .addInterceptor(provideAuthHeader)
        .build()

    private val apiUrl = Retrofit.Builder()
        .baseUrl("http://192.168.43.82:8080")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ParkingApi = apiUrl.create(ParkingApi::class.java)

}

class AuthInterceptor @Inject constructor(private val authHeader: IncidentHolder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", authHeader.credential)
            .build()
        return chain.proceed(modifiedRequest)
    }
}*/
