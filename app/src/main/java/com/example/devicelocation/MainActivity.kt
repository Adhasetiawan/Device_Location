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
                var formatMM = "01"
                var formatdd = "01"
                var formatHH = "00"
                var formatmm = "00"

                //getdate & format date
                val dd = dateTime.get(Calendar.DAY_OF_MONTH)
                val MM = dateTime.get(Calendar.MONTH).plus(1)
                val yyyy = dateTime.get(Calendar.YEAR)
                if (MM < 10 && dd < 10) {
                    formatMM = "0" + MM.toString()
                    formatdd = "0" + dd.toString()
                } else if (MM < 10) {
                    formatMM = "0" + MM.toString()
                } else if (dd < 10) {
                    formatdd = "0" + dd.toString()
                } else {
                    formatMM = MM.toString()
                    formatdd = dd.toString()
                }

                //gettime and format time
                val HH = dateTime.get(Calendar.HOUR_OF_DAY)
                val mm = dateTime.get(Calendar.MINUTE)
                if (HH < 10 && mm < 10) {
                    formatHH = "0" + HH.toString()
                    formatmm = "0" + mm.toString()
                } else if (HH < 10) {
                    formatHH = "0" + HH.toString()
                } else if (mm < 10) {
                    formatmm = "0" + mm.toString()
                } else {
                    formatHH = HH.toString()
                    formatmm = mm.toString()
                }

                txt_time.text =
                    yyyy.toString() + "-" + formatMM + "-" + formatdd + " " + formatHH + ":" + formatmm

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