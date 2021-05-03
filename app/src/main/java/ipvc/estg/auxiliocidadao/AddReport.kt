package ipvc.estg.auxiliocidadao

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ipvc.estg.auxiliocidadao.api.EndPoints
import ipvc.estg.auxiliocidadao.api.Report
import ipvc.estg.auxiliocidadao.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_add_report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddReport : AppCompatActivity() {

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report)



        fab_voltar2.setOnClickListener { _ ->
            val intent = Intent(this, Maps::class.java)

            startActivity(intent)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)

            return
        }
        else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {

                    lastLocation = location

                    val lat = location.latitude
                    val long = location.longitude

                    val latShow = findViewById<TextView>(R.id.textViewLatSub)
                    latShow.setText(lat.toString())

                    val lngShow = findViewById<TextView>(R.id.textViewLngSubs)
                    lngShow.setText(long.toString())
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun enviar(view: View) {

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    var id: Int? = 0

                    val sharedPref: SharedPreferences = getSharedPreferences(
                            getString(R.string.guarda_login), Context.MODE_PRIVATE
                    )

                    if (sharedPref != null){
                        id = sharedPref.all[getString(R.string.id1)] as Int?
                    }

                    val problem = findViewById<EditText>(R.id.ediTextProblem)
                    val usersId = id
                    val users_id: Int = usersId!!
                    val latitude = lastLocation.latitude
                    val longitude = lastLocation.longitude

                    // FOTO
                    val photo = findViewById<ImageView>(R.id.imageViewPhoto)

                    //DATA
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ISO_DATE
                    val formatted = current.format(formatter)
                    val data1 = formatted.toString()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.setReport(problem.text.toString(), latitude, longitude, "foto1", data1, users_id)

                    call.enqueue(object : Callback<Report> {
                        override fun onResponse(call: Call<Report>?, response: Response<Report>?) {

                            if (response!!.isSuccessful) {

                                val toast = Toast.makeText(applicationContext, R.string.sucesso, Toast.LENGTH_SHORT)
                                toast.show()
                                problem.setText("")

                            }
                        }

                        override fun onFailure(call: Call<Report>?, t: Throwable?) {
                            Toast.makeText(applicationContext, t!!.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    fun escolheFoto(view: View) {






    }



}