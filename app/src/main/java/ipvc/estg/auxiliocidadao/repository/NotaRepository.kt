package ipvc.estg.auxiliocidadao.repository

import androidx.lifecycle.LiveData
import ipvc.estg.auxiliocidadao.entitie.Nota
import ipvc.estg.auxiliocidadao.dao.NotaDao

class NotaRepository(private val notaDao: NotaDao) {

    val allItems: LiveData<List<Nota>> = notaDao.getall()

    fun get(id: Long):LiveData<Nota> {
        return notaDao.get(id)
    }

    suspend fun update(item: Nota) {
        notaDao.update(item)
    }

    suspend fun insert(item: Nota) {
        notaDao.insert(item)
    }

    suspend fun delete(item: Nota) {
        notaDao.delete(item)
    }
}