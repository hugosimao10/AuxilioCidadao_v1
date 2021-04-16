package ipvc.estg.auxiliocidadao

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.auxiliocidadao.entity.Nota
import ipvc.estg.auxiliocidadao.viewModel.NotasViewModel
import kotlinx.android.synthetic.main.activity_add_notas_pessoais.*

class AddNotasPessoais : AppCompatActivity() {

    private lateinit var notaViewModel: NotasViewModel
    private var notaId: Long = 0L
    private var isEdit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_notas_pessoais)

        fab_voltar1.setOnClickListener { _ ->
            val intent = Intent(this, NotasPessoais::class.java)
            notas_titulo.setText("")
            notas_descricao.setText("")
            startActivity(intent)
        }

        notaViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)

        if (intent.hasExtra("nota_id")) {
            notaId = intent.getLongExtra("nota_id", 0L)

            notaViewModel.get(notaId).observe(this, Observer {

                val viewId = findViewById<TextView>(R.id.nota_id)
                val viewTitulo = findViewById<EditText>(R.id.notas_titulo)
                val viewDescricao = findViewById<EditText>(R.id.notas_descricao)

                if (it != null) {

                    viewId.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_notas, 0, 0, 0);
                    
                    viewId.text = it.id.toString()
                    viewTitulo.setText(it.titulo)
                    viewDescricao.setText(it.descricao)

                }
            })
            isEdit = true
        }

        val btnSave = btnSave
        btnSave.setOnClickListener { view ->
            val id = 0L
            val titulo = notas_titulo.text.toString()
            val descricao = notas_descricao.text.toString()

            if (titulo.isBlank() or titulo.isEmpty()) {
                Snackbar.make(view, "O título não pode ficar vazio!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            else if (descricao.isBlank() or descricao.isEmpty()) {
                Snackbar.make(view, "A descrição não pode ficar vazia!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

            else {
                val item = Nota(id = id, titulo = titulo, descricao = descricao)
                notaViewModel.insert(item)
                finish()
            }
        }

        val btnUpdate = btnUpdate
        btnUpdate.setOnClickListener { view ->
            val id = nota_id.text.toString().toLong()
            val titulo = notas_titulo.text.toString()
            val descricao = notas_descricao.text.toString()

            if (titulo.isBlank() or titulo.isEmpty()) {
                Snackbar.make(view, "O título não pode ficar vazio!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            else if (descricao.isBlank() or descricao.isEmpty()) {
                Snackbar.make(view, "A descrição não pode ficar vazia!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            else {
                val item = Nota(id = id, titulo = titulo, descricao = descricao)
                notaViewModel.update(item)
                finish()
            }
        }

        val btnDelete = btnDelete
        btnDelete.setOnClickListener {
            val id = nota_id.text.toString().toLong()
            val titulo = notas_titulo.text.toString()
            val descricao = notas_descricao.text.toString()

            val item = Nota(id = id, titulo = titulo, descricao = descricao)
            notaViewModel.delete(item)
            finish()
        }

        if (isEdit) {

            btnSave.visibility = View.GONE
        } else {

            btnUpdate.visibility = View.GONE
            btnDelete.visibility = View.GONE
        }
    }
}
