package ipvc.estg.auxiliocidadao

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.auxiliocidadao.api.EndPoints
import ipvc.estg.auxiliocidadao.api.OutputPost
import ipvc.estg.auxiliocidadao.api.Report
import ipvc.estg.auxiliocidadao.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_add_notas_pessoais.*
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Maps : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var reports: List<Report>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.guarda_login), Context.MODE_PRIVATE
        )

        val button: Button = findViewById(R.id.butLogout)
        button.setOnClickListener {

            val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.guarda_login), Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                putInt(getString(R.string.id1), 0)
                apply()
            }

            val intent = Intent(this@Maps, Inicial::class.java)
            startActivity(intent)
            finish()
        }


        // COORDENADAS DE VIANA PARA USAR COMO CENTRAL NA CAMERA

        val viana = LatLng(41.6946, -8.83016)

        // BOTAO DE ADICIONAR REPORT

        fab_addReport.setOnClickListener { _ ->
            val intent = Intent(this, AddReport::class.java)
            startActivity(intent)
        }

        //  CONECTAR AO WEBHOST E BUSCAR TODOS OS REPORTES EFETUADOS PARA ADICIONAR NO MAPA

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()
        var position: LatLng

        call.enqueue(object : Callback<List<Report>> {
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                if (response.isSuccessful) {
                    reports = response.body()!!

                    // CAMERA INICIAL A APONTAR PARA VIANA E COM ZOOM ADEQUADO

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(viana))
                    mMap.animateCamera( CameraUpdateFactory.zoomTo(9.0f));

                    for (report in reports) {

                        position = LatLng(report.lat, report.lng)

                        if(report.users_id == R.string.id1) {

                            mMap.addMarker(MarkerOptions().position(position).title(report.problem).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                       }
                        else{
                            mMap.addMarker(MarkerOptions().position(position).title(report.problem).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

                        }

                    }
                }
                else{
                    Toast.makeText(this@Maps, R.string.erroInsucesso, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                Toast.makeText(this@Maps, R.string.erroOnFailure, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

/*
        // Add a marker in Sydney and move the camera

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
*/



    }
}