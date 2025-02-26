package com.bazaroff_alexey.newroutes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MakeRouteActivity : AppCompatActivity() {


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Intent", "Попали в MakeRouteActivity")

//      enableEdgeToEdge()
        setContentView(R.layout.activity_make_route)
        val uid = Utils.getUidFromSharedPreferences(this)


        if (uid != null) {
            // используем UID
            Log.d("MakeRouteActivity", "UID: $uid")
        } else {
            // Если UID не найден, например, перенаправляем на экран логина
            Toast.makeText(this, "Пожалуйста, войдите снова", Toast.LENGTH_SHORT).show()
        }

        val myLocation = intent.getStringExtra("selfLocation")
        val finLocation = intent.getStringExtra("finLocation")
        Log.d("Intent", "myLocation: ${myLocation}")
        Log.d("Intent", "finLocation: ${finLocation}")
        // Формируем URL с userId
        val url =
            "http://10.0.2.2:8000/route/${uid}/${myLocation},${finLocation}?"
        // val url = "http://89.104.66.155/route/${myLocation},${finLocation}?userId=${userId}"

        Log.d("Intent", "Загрузка URL: $url")
        val webView = findViewById<WebView>(R.id.webview)


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().useWideViewPort = true;
        webView.getSettings().setDomStorageEnabled(true);

        webView.webViewClient = WebViewClient() // Это предотвратит открытие ссылок в браузер
        webView.loadUrl(url)
    }

}

