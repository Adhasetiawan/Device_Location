package com.example.devicelocation

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var lat: Double = 0.0
    private var lng: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        autolocate()
        relocate()
        selectionact()
    }

    private fun autolocate() {
        //permission
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
        } else {
            buildLocationRequest()
            buildLocationCallback()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            //trigger
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1000
                )
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun relocate() {
        btn_locate.setOnClickListener {
            autolocate()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.size > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                val location = p0.locations.get(p0.locations.size - 1)
                lat = location.latitude
                lng = location.longitude

                txt_lat.text = lat.toString()
                txt_long.text = lng.toString()
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    private fun selectionact() {
        val target_lat : Double = -0.5063700
        val target_lng : Double = 117.1605300

        txt_latitude.text = target_lat.toString()
        txt_longitude.text = target_lng.toString()

        btn_toast.setOnClickListener {
            if(radio_Office.isChecked){
                val start = Location("locationStart")
                start.latitude = lat
                start.longitude = lng

                val end = Location("locationEnd")
                end.latitude = target_lat
                end.longitude = target_lng

                var distance = start.distanceTo(end)
                if(distance <= 20.0){
                    Toast.makeText(this, "anda bisa absen", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "anda tidak bisa absen", Toast.LENGTH_SHORT).show()
                }

            }else if (radio_wfh.isChecked){
                Toast.makeText(this, "your location now at lat : " + lat + " lng : " + lng, Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun distanceCalcualte(){
//        var lati_1 = lati_1.text
//        var long_1 = long_1.text
//
//        var lati_2 = lati_2.text
//        var long_2 = long_2.text
//
//            val point_1 = Location("locationA")
//            point_1.latitude = lati_1.toString().toDouble()
//            point_1.longitude = long_1.toString().toDouble()
//
//            val point_2 = Location("locationA")
//            point_2.latitude = lati_2.toString().toDouble()
//            point_2.longitude = long_2.toString().toDouble()
//
//            var distance = point_1.distanceTo(point_2).toDouble()
//
//            if (distance <= 100.0 ){
//                Toast.makeText(this, "anda bisa absen, jarak anda : " + distance, Toast.LENGTH_SHORT).show()
//        }else{
//                Toast.makeText(this, "anda tidak bisa absen " + distance, Toast.LENGTH_SHORT).show()
//            }
//        }
}