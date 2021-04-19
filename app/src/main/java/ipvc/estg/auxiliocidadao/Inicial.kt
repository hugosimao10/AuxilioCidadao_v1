package ipvc.estg.auxiliocidadao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ipvc.estg.auxiliocidadao.api.EndPoints
import ipvc.estg.auxiliocidadao.api.ServiceBuilder
import ipvc.estg.auxiliocidadao.api.User
import kotlinx.android.synthetic.main.activity_inicial.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Inicial : AppCompatActivity() {

    private lateinit var user: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)

        // BOTAO QUE CONFIRMA DADOS DE LOGIN E LEVA PARA O MAPA

        val button: Button = findViewById(R.id.buttonLogin)
        button.setOnClickListener {

            var editTextID = findViewById(R.id.editTextId) as EditText
            var editTextPassword = findViewById(R.id.editTextPassword) as EditText

            val userInserido = editTextID.text.toString()
            var passInserida = editTextPassword.text.toString()

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getUsers()

            call.enqueue(object : Callback<List<User>>{
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if(response.isSuccessful){
                        user = response.body()!!

                        for (user in user) {

                            if(user.username == userInserido && user.password == passInserida){

                                Toast.makeText(this@Inicial, R.string.LoginOK, Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@Inicial, Maps::class.java)
                                startActivity(intent)
                                finish()
                                break

                            }
                            else{
                                Toast.makeText(this@Inicial, R.string.LoginFalhado, Toast.LENGTH_SHORT).show()
                            }

                        }

                    }
                    else{
                        Toast.makeText(this@Inicial, R.string.erroInsucesso, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Toast.makeText(this@Inicial, R.string.erroOnFailure, Toast.LENGTH_SHORT).show()
                }
            })





        }

        // BOTAO PARA IR PARA AS NOTAS

        val button2: Button = findViewById(R.id.buttonNotas)
        button2.setOnClickListener {
            val intent = Intent(this, NotasPessoais::class.java)
            startActivity(intent)
        }
    }
}