package ipvc.estg.auxiliocidadao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notas")
data class Nota(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val titulo: String,
    val descricao: String

)

