package ipvc.estg.auxiliocidadao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar

class NotasPessoais : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas_pessoais)

        val fab: View = findViewById(R.id.fab1)
        fab.setOnClickListener { view ->
            val intent = Intent(this, AddNotasPessoais::class.java)
            startActivity(intent)
        }
        val button3: Button = findViewById(R.id.buttonVoltar2)
        button3.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}