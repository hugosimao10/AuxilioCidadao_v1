package ipvc.estg.auxiliocidadao.api


data class Report(
        val id: Int,
        val problem: String,
        val lat: Double,
        val lng: Double,
        val photo: String,
        val date: String,
        val users_id: Int
)
