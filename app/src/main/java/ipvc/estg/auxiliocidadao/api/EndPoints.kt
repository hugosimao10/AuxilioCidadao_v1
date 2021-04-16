package ipvc.estg.auxiliocidadao.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EndPoints {

    @GET("/reports/")
    fun getReports(): Call<List<Report>>

    @GET("/reports/{id}")
    fun getReportById(@Path("id") id: Int): Call<Report>


}