package ipvc.estg.auxiliocidadao.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("api/reports")
    fun getReports(): Call<List<Report>>

    @GET("api/reports/{id}")
    fun getReportById(@Path("id") id: Int): Call<Report>

    @GET("api/users/")
    fun getUsers(): Call<List<User>>

    @GET("api/users/{id}")
    fun getUsersById(@Path("id") id: Int): Call<User>
}