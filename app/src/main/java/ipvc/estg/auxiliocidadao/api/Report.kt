package ipvc.estg.auxiliocidadao.api

import android.graphics.Point
import java.sql.Blob
import java.sql.Date

data class Report(
    val id: Int,
    val problem: String,
    val coordinates: Point,
    val photo: Blob,
    val date: Date,
    val users_id: Int
)

data class Coordinates(
    val lat: Float,
    val lon: Float
)