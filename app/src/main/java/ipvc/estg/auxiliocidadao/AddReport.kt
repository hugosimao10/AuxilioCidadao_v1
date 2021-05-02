package ipvc.estg.auxiliocidadao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_report.*

class AddReport : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report)

        fab_voltar2.setOnClickListener { _ ->
            val intent = Intent(this, Maps::class.java)

            startActivity(intent)
        }

        





    }
}