package ipvc.estg.auxiliocidadao.api

import java.sql.Blob
import java.sql.Date

data class Report(
        val id: Int,
        val problem: String,
        val lat: Double,
        val lng: Double,
        val photo: Blob,
        val date: Date,
        val users_id: Int
)

