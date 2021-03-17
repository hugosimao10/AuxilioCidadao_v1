package ipvc.estg.auxiliocidadao.repository

import androidx.lifecycle.LiveData
import ipvc.estg.auxiliocidadao.room.Nota
import ipvc.estg.auxiliocidadao.room.NotaDao

class NotaRepository(private val datarecordDao: NotaDao) {

    val allItems: LiveData<List<Nota>> = datarecordDao.getall()

    fun get(id: Long):LiveData<Nota> {
        return datarecordDao.get(id)
    }

    suspend fun update(item: Nota) {
        datarecordDao.update(item)
    }

    suspend fun insert(item: Nota) {
        datarecordDao.insert(item)
    }

    suspend fun delete(item: Nota) {
        datarecordDao.delete(item)
    }
}