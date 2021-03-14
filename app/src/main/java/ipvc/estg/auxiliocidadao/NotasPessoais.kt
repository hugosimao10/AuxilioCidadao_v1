package ipvc.estg.auxiliocidadao

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_notas_pessoais.*
import kotlinx.android.synthetic.main.content.*

private const val TAG = "NotasPessoais"  // TAG for Logs, if the need them

class NotasPessoais : AppCompatActivity() {

    private lateinit var notaViewModel: NotasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the layout
        setContentView(R.layout.activity_notas_pessoais)


        // Set an action for the FAB: in particular, this will start a new activity
        fab_add.setOnClickListener { _ ->
            val intent = Intent(this, AddNotasPessoais::class.java)
            startActivity(intent)
        }


        val recyclerView = notas_list
        val adapter = NotaAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        /* We need the data to populate the list with. For this we will retrieve the ViewModel
           that we have defined, which in our case is a `DataRecordViewModel::class.java`, from
           the ViewModelProvider service.
        */
        notaViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)

        /* Simply associate an observer with each of the items contained in the viewmodel.

           Notice that this can be done because `allItems` in the `DataRecordViewModel` is a `LiveData`
           object.

           The method `setItems` of the `DataRecordAdapter` class, takes care of populating each
           line of the RecyclerView, according to the layout specified in `datarecord_viewholder`.

           This is also where we set any actions (click, longclick...) per item in the list.

        */
        notaViewModel.allItems.observe(this, Observer { items ->
            items?.let { adapter.setItems(it) }
        })
    }
}
