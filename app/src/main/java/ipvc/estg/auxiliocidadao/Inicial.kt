package ipvc.estg.auxiliocidadao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Inicial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)

        val button: Button = findViewById(R.id.buttonLogin)
        button.setOnClickListener {
            val intent = Intent(this, Maps::class.java)
            startActivity(intent)
            finish()
        }

        val button2: Button = findViewById(R.id.buttonNotas)
        button2.setOnClickListener {
            val intent = Intent(this, NotasPessoais::class.java)
            startActivity(intent)
        }
    }
}