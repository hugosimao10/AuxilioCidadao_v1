package ipvc.estg.auxiliocidadao

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.auxiliocidadao.api.EndPoints
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
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var results = FloatArray(1)
    var idPassa = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback(){

            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)

                if (p0 != null) {
                    lastLocation = p0.lastLocation
                }
                else{
                    Toast.makeText(this@Maps, "p0 is null", Toast.LENGTH_SHORT).show()
                }

                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)


            }
        }

        createLocationRequest()

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.guarda_login), Context.MODE_PRIVATE
        )

        val id = sharedPref.all[getString(R.string.id1)] as Int?

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

                    val viana = LatLng(41.6946, -8.83016)

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(viana, 8.0f))
                    for (report in reports) {

                        position = LatLng(report.lat, report.lng)
                        val b = report.id.toString()

                        if(report.users_id == id) {

                            mMap.addMarker(MarkerOptions().position(position).title(b).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                       }
                        else{
                            mMap.addMarker(MarkerOptions().position(position).title(b).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

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


    // UPDATES DA LOCALIZACAO
    private fun startLocationUpdates() {

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            1)
            return

        }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

    }

    // CRIA PEDIDO DE LOCALIZACAO
    private fun createLocationRequest() {
            locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    /**
     * ON MAP READY
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setUpMap()

        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                Toast.makeText(this@Maps, R.string.markerCLick, Toast.LENGTH_LONG).show()
            } else {

                val intent = Intent(this, EditDeleteReport::class.java)
                intent.putExtra(EXTRA_PROBLEMID, marker.title)
                startActivity(intent)


            }
            true
        }

    }

    fun setUpMap() {
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)

            return
        }
        else{

            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this){ location ->

                if(location!= null){
                    lastLocation = location
                    val currentLatLan = LatLng(location.latitude,location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLan, 8.0f))
                }
                }


        }
    }


    fun calculateDistance(lat1: Double, lng1: Double, lat2:Double, lng2:Double) : Float {

        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        return results[0]
    }

    fun filtro5km(view: View) {

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 123)

            return
        } else {

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    mMap.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getReports()
                    var position: LatLng
                    var id: Int? = 0

                    val sharedPref: SharedPreferences = getSharedPreferences(
                            getString(R.string.guarda_login), Context.MODE_PRIVATE
                    )

                    if (sharedPref != null) {
                        id = sharedPref.all[getString(R.string.id1)] as Int?
                    }

                    call.enqueue(object : Callback<List<Report>> {
                        override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {

                            if (response.isSuccessful) {

                                reports = response.body()!!

                                for (report in reports) {
                                    position = LatLng(report.lat, report.lng)

                                    if (calculateDistance(lastLocation.latitude, lastLocation.longitude, report.lat, report.lng) < 5000) {
                                        mMap.addMarker(MarkerOptions().position(position).title(report.problem + report.date).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                                    }


                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                            Toast.makeText(this@Maps, getString(R.string.erroOnFailure), Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }
        }
    }


    fun filtro20km(view: View) {

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 123)

            return
        } else {

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    mMap.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getReports()
                    var position: LatLng
                    var id: Int? = 0

                    val sharedPref: SharedPreferences = getSharedPreferences(
                            getString(R.string.guarda_login), Context.MODE_PRIVATE
                    )

                    if (sharedPref != null) {
                        id = sharedPref.all[getString(R.string.id1)] as Int?
                    }

                    call.enqueue(object : Callback<List<Report>> {
                        override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {

                            if (response.isSuccessful) {

                                reports = response.body()!!

                                for (report in reports) {
                                    position = LatLng(report.lat, report.lng)

                                    if (calculateDistance(lastLocation.latitude, lastLocation.longitude, report.lat, report.lng) < 20000) {
                                        mMap.addMarker(MarkerOptions().position(position).title(report.problem + report.date).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                                    }

                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                            Toast.makeText(this@Maps, getString(R.string.erroOnFailure), Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }
        }
    }

    fun filtroObras(view: View) {

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 123)

            return
        } else {

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    mMap.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getReports()
                    var position: LatLng
                    var problema: String
                    var id: Int? = 0

                    val sharedPref: SharedPreferences = getSharedPreferences(
                            getString(R.string.guarda_login), Context.MODE_PRIVATE
                    )

                    if (sharedPref != null) {
                        id = sharedPref.all[getString(R.string.id1)] as Int?
                    }

                    call.enqueue(object : Callback<List<Report>> {
                        override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {

                            if (response.isSuccessful) {

                                reports = response.body()!!

                                for (report in reports) {
                                    position = LatLng(report.lat, report.lng)
                                    problema = report.problem

                                    if(report.problem.contains("Obra") || report.problem.contains("obra")) {
                                        mMap.addMarker(MarkerOptions().position(position).title(report.problem + report.date).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))

                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                            Toast.makeText(this@Maps, getString(R.string.erroOnFailure), Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }
        }

    }
    fun filtroAcidentes(view: View) {

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 123)

            return
        } else {

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    mMap.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getReports()
                    var position: LatLng
                    var problema: String
                    var id: Int? = 0

                    val sharedPref: SharedPreferences = getSharedPreferences(
                            getString(R.string.guarda_login), Context.MODE_PRIVATE
                    )

                    if (sharedPref != null) {
                        id = sharedPref.all[getString(R.string.id1)] as Int?
                    }

                    call.enqueue(object : Callback<List<Report>> {
                        override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {

                            if (response.isSuccessful) {

                                reports = response.body()!!

                                for (report in reports) {
                                    position = LatLng(report.lat, report.lng)
                                    problema = report.problem

                                    if(report.problem.contains("Acidente") || report.problem.contains("acidente")) {
                                        mMap.addMarker(MarkerOptions().position(position).title(report.problem + report.date).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))

                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                            Toast.makeText(this@Maps, getString(R.string.erroOnFailure), Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }
        }


    }

    companion object {
        const val EXTRA_PROBLEMID = "valor"

    }


}