package ipvc.estg.auxiliocidadao.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.auxiliocidadao.repository.NotaRepository
import ipvc.estg.auxiliocidadao.db.AppRoomDatabase
import ipvc.estg.auxiliocidadao.entitie.Nota
import kotlinx.coroutines.launch

class NotasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotaRepository
    val allItems: LiveData<List<Nota>>

    init {
        val dao = AppRoomDatabase.getDatabase(application).notaDao()
        repository = NotaRepository(dao)
        allItems = repository.allItems
    }

    fun insert(item: Nota) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: Nota) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: Nota) = viewModelScope.launch {
        repository.delete(item)
    }

    fun get(id: Long) = repository.get(id)
}