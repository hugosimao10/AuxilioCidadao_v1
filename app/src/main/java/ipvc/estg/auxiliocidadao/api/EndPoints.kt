package ipvc.estg.auxiliocidadao.api

import retrofit2.Call
import retrofit2.http.*
import java.sql.Blob
import java.sql.Date

interface EndPoints {

    @GET("api/reports")
    fun getReports(): Call<List<Report>>

    @GET("api/reports/{id}")
    fun getReportById(@Path("id") id: Int): Call<Report>

    @FormUrlEncoded
    @POST("api/users/login")
    fun getUsers(@Field("username") first: String?, @Field("password")second: String?): Call<OutputPost>

    @FormUrlEncoded
    @POST("api/reports/insert")
    fun setReport(@Field("problem") first: String?, @Field("lat")second: Double, @Field("lng")third: Double,
                  @Field("photo")fourth: String, @Field("date")fifth: String, @Field("users_id")sixth: Int): Call<Report>


    @FormUrlEncoded
    @POST("api/reports/update/{id}")
    fun updateReport(@Path("id") id: Int, @Field("problem") problem: String?): Call<Report>


    @POST("api/reports/delete/{id}")
    fun deleteReport(@Path("id") id: Int): Call<Report>

}