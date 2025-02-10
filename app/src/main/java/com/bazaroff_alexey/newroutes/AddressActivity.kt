package com.bazaroff_alexey.newroutes

import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bazaroff_alexey.newroutes.databinding.ActivityAdressBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource


class AddressActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityAdressBinding

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAdressBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions: Map<String, Boolean> ->
            when {
                permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) || permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                ) -> {
                    Toast.makeText(this, "Доступ к локации преоставлен.", Toast.LENGTH_SHORT).show()

                    if (isLocationEnabled()) {
                        val result = fusedLocationClient.getCurrentLocation(
                            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                            CancellationTokenSource().token
                        )
                        result.addOnCompleteListener { task ->
                            val locationResult = task.result
                            if (locationResult != null) {
                                val location =
                                    "${locationResult.latitude},${locationResult.longitude}"
                                toMakeRoute(location)

                            } else {
                                Toast.makeText(
                                    this,
                                    "Не удалось получить местоположение.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Пожалуйста, ВКЛючите локацию.", Toast.LENGTH_SHORT)
                            .show()
                        createLocationRequest()
                    }
                }

                else -> {
                    Toast.makeText(this, "Нет доступа к локации.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnAddress.setOnClickListener {
            locationPermissionRequest.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun createLocationRequest() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            10000
        ).setMinUpdateIntervalMillis(5000).build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            //TODO
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        this,
                        100
                    )
                } catch (sendEx: Exception) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    private fun toMakeRoute(location: String) {
        val makeRouteActivity = Intent(
            this@AddressActivity,
            MakeRouteActivity::class.java
        )
        makeRouteActivity.putExtra("selfLocation", location)
        startActivity(makeRouteActivity)
        finish();
    }
}


