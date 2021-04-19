package ipvc.estg.auxiliocidadao.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("api/reports")
    fun getReports(): Call<List<Report>>

    @GET("api/reports/{id}")
    fun getReportById(@Path("id") id: Int): Call<Report>

    @FormUrlEncoded
    @POST("api/users/login")
    fun getUsers(@Field("username") first: String?, @Field("password")second: String?): Call<OutputPost>

}