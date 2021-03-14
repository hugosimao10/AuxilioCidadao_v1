package ipvc.estg.auxiliocidadao

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_notas_pessoais.*

class NotasPessoais : AppCompatActivity() {

    private lateinit var notaViewModel: NotasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notas_pessoais)

        fab_add.setOnClickListener { _ ->
            val intent = Intent(this, AddNotasPessoais::class.java)
            startActivity(intent)
        }

        fab_voltar.setOnClickListener { _ ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        val recyclerView = notas_list
        val adapter = NotaAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        notaViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)

        notaViewModel.allItems.observe(this, Observer { items ->
            items?.let { adapter.setItems(it) }
        })
    }
}
