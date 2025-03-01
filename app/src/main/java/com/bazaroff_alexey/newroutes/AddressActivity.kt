package com.bazaroff_alexey.newroutes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddressActivity : AppCompatActivity() {
    //variables for my location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityAdressBinding


    //variables for finish location
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var locationGlobalMy: String
    private lateinit var locationGlobalFin: String
    private val addressList = mutableListOf<String>()
    private lateinit var uid: String

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isLargeText = sharedPreferences.getBoolean("largeText", false)
        setTheme(if (isLargeText) R.style.LargeFontTheme else R.style.NormalFontTheme)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val uid = Utils.getUidFromSharedPreferences(this)


        if (uid != null) {
            // используем UID
            Log.d("AddressActivity", "UID: $uid")
        } else {
            // Если UID не найден, например, перенаправляем на экран логина
            Toast.makeText(this, "Пожалуйста, войдите снова", Toast.LENGTH_SHORT).show()
        }
        Log.d("AddressActivity", "Полученный USER_ID: $uid") // Добавь лог для проверки

        locationGlobalMy = "-999"
        locationGlobalFin = "-999"
        //for my location
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
                    Toast.makeText(this, "Доступ к локации предоставлен.", Toast.LENGTH_SHORT)
                        .show()

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
                                locationGlobalMy = location

                                Log.d("Retrofit", "locationGlobal My: $locationGlobalMy")

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


            val query = binding.autoCompleteTextView.text.toString()

            if (query.isNotEmpty()) {
                searchAddress(query, locationPermissionRequest)

            } else {
                Toast.makeText(this, "Введите адрес", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun searchAddress(
        query: String,
        locationPermissionRequest: ActivityResultLauncher<Array<String>>
    ) {
        Log.d("Retrofit", "Отправка запроса с query: $query")  // Лог перед запросом

        RetrofitAPI.instance.searchAddress(query)
            .enqueue(object : Callback<AddressResponse> { // <-- исправили тип
                override fun onResponse(
                    call: Call<AddressResponse>,
                    response: Response<AddressResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d("Retrofit", "JSON Response: $result")

                        if (result != null) {
                            val lat = result.lat
                            val lon = result.lon
                            val address = result.address
                            Log.d("Retrofit", "Координаты: lat=$lat, lon=$lon, address=$address")

                            Toast.makeText(
                                this@AddressActivity,
                                "Координаты: $lat, $lon\nАдрес: $address",
                                Toast.LENGTH_LONG
                            ).show()

                            locationPermissionRequest.launch(
                                arrayOf(
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                            locationGlobalFin = "${lat},${lon}"
                            Log.d("Retrofit", "locationGlobal fin: $locationGlobalFin")
                            toMakeRoute()


                        } else {
                            Log.w("Retrofit", "Ответ пустой")
                            Toast.makeText(
                                this@AddressActivity,
                                "Адрес не найден",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Log.e("Retrofit", "Ошибка запроса: ${response.code()}")
                        Toast.makeText(
                            this@AddressActivity,
                            "Ошибка запроса: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                    Log.e("Retrofit", "Ошибка сети: ${t.message}", t)
                    Toast.makeText(
                        this@AddressActivity,
                        "Ошибка сети: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
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

    private fun toMakeRoute() {
        Log.d("Intent", "Попали в MakeRoute")
        if (locationGlobalFin == "-999" || locationGlobalMy == "-999") {
            return
        }
        try {
            val makeRouteActivity =
                Intent(
                    this@AddressActivity,
                    MakeRouteActivity::class.java
                )
            makeRouteActivity.putExtra("selfLocation", locationGlobalMy)
            makeRouteActivity.putExtra("finLocation", locationGlobalFin)
            startActivity(makeRouteActivity)
        } catch (e: Exception) {
            Log.d("Intent", "Ошибка при создании Intent: ${e.toString()}")
        }

    }
}


