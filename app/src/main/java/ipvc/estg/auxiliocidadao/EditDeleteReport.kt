package ipvc.estg.auxiliocidadao

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import ipvc.estg.auxiliocidadao.api.EndPoints
import ipvc.estg.auxiliocidadao.api.Report
import ipvc.estg.auxiliocidadao.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_edit_delete_report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditDeleteReport : AppCompatActivity() {

    var idReportEdit = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete_report)


        fab_voltar3.setOnClickListener { _ ->
            val intent = Intent(this, Maps::class.java)
            startActivity(intent)
        }

        var id: Int? = 0

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.guarda_login), Context.MODE_PRIVATE
        )

        if (sharedPref != null) {
            id = sharedPref.all[getString(R.string.id1)] as Int?
        }

        val idInt: Int = id!!

        val intentProblem: Bundle? = intent.extras
        idReportEdit = intentProblem?.getString(Maps.EXTRA_PROBLEMID)!!.toString()

        val idParaEditar = idReportEdit.toInt()

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReportById(idParaEditar)

            call.enqueue(object : Callback<Report> {
                override fun onResponse(call: Call<Report>, response: Response<Report>) {
                    if (response.isSuccessful) {
                        val c: Report = response.body()!!

                        if (c.users_id == idInt) {

                            val latView = findViewById<TextView>(R.id.textViewLatSub2)
                            latView.setText(c.lat.toString())
                            val lngView = findViewById<TextView>(R.id.textViewLngSubs2)
                            lngView.setText(c.lng.toString())
                            val probSet = findViewById<EditText>(R.id.ediTextProblem2)
                            probSet.setText(c.problem.toString())

                        } else {

                            val butDelt = findViewById<Button>(R.id.buttonDelete)
                            val butEdt = findViewById<Button>(R.id.buttonEdit)
                            val butEdtFoto = findViewById<Button>(R.id.buttoneditPhoto2)

                            butDelt.isVisible = false
                            butEdt.isVisible = false
                            butEdtFoto.isVisible = false

                            val latView = findViewById<TextView>(R.id.textViewLatSub2)
                            latView.setText(c.lat.toString())
                            val lngView = findViewById<TextView>(R.id.textViewLngSubs2)
                            lngView.setText(c.lng.toString())
                            val probSet = findViewById<EditText>(R.id.ediTextProblem2)
                            probSet.setText(c.problem.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<Report>, t: Throwable) {
                    Toast.makeText(
                        this@EditDeleteReport,
                        R.string.erroOnFailure,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    fun editReport(view: View) {

        val intentProblem: Bundle? = intent.extras
        idReportEdit = intentProblem?.getString(Maps.EXTRA_PROBLEMID)!!.toString()

        val idParaEditar = idReportEdit.toInt()

        val newProblem = ediTextProblem2.text.toString()

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.updateReport(idParaEditar, newProblem)

        call.enqueue(object : Callback<Report> {
            override fun onResponse(call: Call<Report>, response: Response<Report>) {

                if (response.isSuccessful) {
                    val toast = Toast.makeText(applicationContext, R.string.sucesso, Toast.LENGTH_SHORT)
                    toast.show()
                    val intent = Intent(this@EditDeleteReport, Maps::class.java)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<Report>, t: Throwable) {
                val toast = Toast.makeText(applicationContext, R.string.sucesso, Toast.LENGTH_SHORT)
                toast.show()
                val intent = Intent(this@EditDeleteReport, Maps::class.java)
                startActivity(intent)
            }


        })
    }
    fun deleteReport(view: View) {

        val intentProblem: Bundle? = intent.extras
        idReportEdit = intentProblem?.getString(Maps.EXTRA_PROBLEMID)!!.toString()

        val idParaEditar = idReportEdit.toInt()

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.deleteReport(idParaEditar)

        call.enqueue(object : Callback<Report> {
            override fun onResponse(call: Call<Report>, response: Response<Report>) {

                if (response.isSuccessful) {

                    val toast = Toast.makeText(applicationContext, R.string.sucesso, Toast.LENGTH_SHORT)
                    toast.show()

                    val intent = Intent(this@EditDeleteReport, Maps::class.java)
                    startActivity(intent)

                }

            }

            override fun onFailure(call: Call<Report>, t: Throwable) {
                Toast.makeText(
                        this@EditDeleteReport,
                        R.string.erroOnFailure,
                        Toast.LENGTH_SHORT
                ).show()
            }


        })


    }

    fun addphoto2(view: View) {}
}


