package com.example.devicelocation

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.format.DateFormat
import android.text.format.Time
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.convertTo
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.days

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
        val target_lat: Double = -0.5063700
        val target_lng: Double = 117.1605300

        txt_latitude.text = target_lat.toString()
        txt_longitude.text = target_lng.toString()

        btn_toast.setOnClickListener {
            if (radio_Office.isChecked) {
                val start = Location("locationStart")
                start.latitude = lat
                start.longitude = lng

                val end = Location("locationEnd")
                end.latitude = target_lat
                end.longitude = target_lng

                var distance = start.distanceTo(end)
                if (distance <= 20.0) {
                    Toast.makeText(this, "anda bisa absen", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "anda tidak bisa absen", Toast.LENGTH_SHORT).show()
                }

                //inisialisasi
                val dateTime = Calendar.getInstance()

                //formating
                var hour = "0"
                var minute = "0"
                var days = "1"
                var month = "1"

                //time
                var getHour = dateTime.get(Calendar.HOUR_OF_DAY)
                var getMinute = dateTime.get(Calendar.MINUTE)
                if (getHour < 10 && getMinute < 10 || getHour < 10 || getMinute < 10){
                    hour = getHour.toString().padStart(2, '0')
                    minute = getMinute.toString().padStart(2, '0')
                }else{
                    hour = getHour.toString()
                    minute = getMinute.toString()
                }

                //date
                var getDays = dateTime.get(Calendar.DAY_OF_MONTH)
                var getMonth = dateTime.get(Calendar.MONTH).plus(1)
                val getYear = dateTime.get(Calendar.YEAR)
                if(getDays < 10 && getMonth < 10 || getDays < 10 || getMonth < 10){
                    days = getDays.toString().padStart(2, '0')
                    month = getMonth.toString().padStart(2, '0')
                } else {
                    days = getDays.toString()
                    month = getMonth.toString()
                }

                txt_time.text = getYear.toString() + "-" + month + "-" + days + " " + hour + ":" + minute

            } else if (radio_wfh.isChecked) {
                Toast.makeText(
                    this,
                    "your location now at lat : " + lat + " lng : " + lng,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}