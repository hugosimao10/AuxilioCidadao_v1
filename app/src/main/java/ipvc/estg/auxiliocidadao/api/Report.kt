package ipvc.estg.auxiliocidadao.api

import android.graphics.Point
import com.google.android.gms.maps.model.LatLng
import java.sql.Blob
import java.sql.Date

data class Report(
    val id: Int,
    val problem: String,
    val coordinates: LatLng,
    val photo: Blob,
    val date: Date,
    val users_id: Int
)
