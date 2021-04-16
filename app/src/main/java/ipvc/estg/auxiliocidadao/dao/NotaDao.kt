package ipvc.estg.auxiliocidadao.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.auxiliocidadao.entity.Nota

@Dao
interface NotaDao {

    @Query("SELECT * from notas")
    fun getall(): LiveData<List<Nota>>

    @Query("SELECT * FROM notas WHERE notas.id == :id")
    fun get(id: Long): LiveData<Nota>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: Nota)

    @Update
    suspend fun update(vararg items: Nota)

    @Delete
    suspend fun delete(vararg items: Nota)
}