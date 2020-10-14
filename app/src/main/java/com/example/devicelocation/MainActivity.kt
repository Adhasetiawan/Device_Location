package com.example.devicelocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

//    private var locationManager: LocationManager? = null
    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var locationGps: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

//        val locationListener: LocationListener = object : LocationListener {
//            override fun onLocationChanged(location: Location) {
//                txt_latitude.text = ("Latitude : " + location.latitude)
//                txt_longitude.text = ("Longitutde : " + location.longitude)
//            }

//            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
//            override fun onProviderEnabled(provider: String?) {}
//            override fun onProviderDisabled(provider: String?) {}
//        }

//        btn_locate.setOnClickListener { view ->
//            try {
//                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 100f, locationListener)
//            } catch (ex: SecurityException){
//                Log.d("myTag", "Security Exception, no location available")
//            }
//        }


        getLocation()
        getPremissionCheck()

        btn_locate.setOnClickListener{
            distanceCalcualte()
        }

    }
    private fun getPremissionCheck(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 111
            )
        else{
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            getLocation()
    }


    private fun getLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            100F,
            object : LocationListener {
                override fun onLocationChanged(location: Location?) {
                    if (location != null) {
                        locationGps = location
                        txt_latitude.setText("Latitude : " + locationGps!!.latitude)
                        txt_longitude.setText("Longitutade : " + locationGps!!.longitude)
                        Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                        Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String?) {}
                override fun onProviderDisabled(provider: String?) {}
            })
        val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (localGpsLocation != null)
            locationGps = localGpsLocation
    }

    private fun distanceCalcualte(){
        var lati_1 = lati_1.text
        var long_1 = long_1.text

        var lati_2 = lati_2.text
        var long_2 = long_2.text

            val point_1 = Location("locationA")
            point_1.latitude = lati_1.toString().toDouble()
            point_1.longitude = long_1.toString().toDouble()

            val point_2 = Location("locationA")
            point_2.latitude = lati_2.toString().toDouble()
            point_2.longitude = long_2.toString().toDouble()

            var distance = point_1.distanceTo(point_2).toDouble()

            if (distance <= 100.0 ){
                Toast.makeText(this, "anda bisa absen, jarak anda : " + distance, Toast.LENGTH_SHORT).show()
        }else{
                Toast.makeText(this, "anda tidak bisa absen " + distance, Toast.LENGTH_SHORT).show()
            }
        }
    }