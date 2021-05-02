package ipvc.estg.auxiliocidadao

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ipvc.estg.auxiliocidadao.api.EndPoints
import ipvc.estg.auxiliocidadao.api.OutputPost
import ipvc.estg.auxiliocidadao.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_inicial.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Inicial : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)

        // SHARED PREFERENCES PARA LOGIN

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.guarda_login), Context.MODE_PRIVATE
        )

        val logincheck = sharedPref.getInt(getString(R.string.id1), 0)

        if(logincheck != 0){
            val intent = Intent(this@Inicial, Maps::class.java)
            startActivity(intent)
            finish()
        }

        // BOTAO QUE CONFIRMA DADOS DE LOGIN E LEVA PARA O MAPA

        val button: Button = findViewById(R.id.buttonLogin)
        button.setOnClickListener {

            var editTextID = findViewById(R.id.editTextId) as EditText
            var editTextPassword = findViewById(R.id.editTextPassword) as EditText

            val userInserido = editTextID.text.toString()
            var passInserida = editTextPassword.text.toString()


            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getUsers(userInserido, passInserida)

            call.enqueue(object : Callback<OutputPost> {
                override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                    if (response.isSuccessful) {

                        val c: OutputPost = response.body()!!

                        if (c.status.toString() == "true") {

                            val sharedPref: SharedPreferences = getSharedPreferences(
                                    getString(R.string.guarda_login), Context.MODE_PRIVATE)
                            with(sharedPref.edit()){
                                putInt(getString(R.string.id1), c.id)
                                commit()
                            }


                            Toast.makeText(this@Inicial, R.string.LoginOK, Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@Inicial, Maps::class.java)
                            startActivity(intent)
                            finish()

                        } else if(userInserido.isEmpty()){

                            Toast.makeText(this@Inicial, R.string.userVazio, Toast.LENGTH_SHORT).show()

                            }
                        else if(passInserida.isEmpty()){

                            Toast.makeText(this@Inicial, R.string.passVazia, Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@Inicial, R.string.LoginFalhado, Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@Inicial, R.string.erroInsucesso, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OutputPost>, t: Throwable) {
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