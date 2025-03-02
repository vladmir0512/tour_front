package com.bazaroff_alexey.newroutes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryRoutesActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RouteAdapter
    private val apiService = RetrofitAPI.instance.getRoutes()

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userId = Utils.getUidFromSharedPreferences(this)
        if (userId.isNullOrEmpty()) {
            Log.e("PreferencesActivity", "Ошибка: UID не передан!")
        } else {
            Log.d("PreferencesActivity", "Получен UID: $userId")
        }
        setContentView(R.layout.activity_history_routes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.historyRoutes)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        recyclerView = findViewById(R.id.recyclerViewRoutes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (userId != null) {
            loadRoutes(userId)
        }

    }

    private fun loadRoutes(userId: String) {
        RetrofitAPI.instance.getUserRoutes(userId = userId).enqueue(object : Callback<List<Route>> {
            override fun onResponse(call: Call<List<Route>>, response: Response<List<Route>>) {
                if (response.isSuccessful) {
                    val routes = response.body() ?: emptyList()
                    adapter = RouteAdapter(routes) { route ->
                        Toast.makeText(
                            this@HistoryRoutesActivity,
                            "Добавление комментария к ${route.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    recyclerView.adapter = adapter
                } else {
                    Log.e("API_ERROR", "Ошибка: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Route>>, t: Throwable) {
                Log.e("API_ERROR", "Ошибка загрузки данных", t)
            }
        })
    }
}