package com.bazaroff_alexey.newroutes

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TechSupportActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val userId = Utils.getUidFromSharedPreferences(this)
        if (userId.isNullOrEmpty()) {
            Log.e("PreferencesActivity", "Ошибка: UID не передан!")
        } else {
            Log.d("PreferencesActivity", "Получен UID: $userId")
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_tech_support)


        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewRoutesLast)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadRoutes(recyclerView)
    }
}

private fun loadRoutes(recyclerView: RecyclerView) {
    RetrofitAPI.instance.getRoutes().enqueue(object : Callback<List<Route>> {
        override fun onResponse(call: Call<List<Route>>, response: Response<List<Route>>) {
            if (response.isSuccessful) {
                val routes = response.body() ?: emptyList()
                val adapterr = RouteUserAdapter(routes) { route ->
                    //todo
                }

                recyclerView.adapter = adapterr
            } else {
                Log.e("API_ERROR", "Ошибка: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<List<Route>>, t: Throwable) {
            Log.e("API_ERROR", "Ошибка загрузки данных", t)
        }
    })
}

