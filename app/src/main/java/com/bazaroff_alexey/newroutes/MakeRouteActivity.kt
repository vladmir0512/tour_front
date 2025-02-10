package com.bazaroff_alexey.newroutes

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class MakeRouteActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      enableEdgeToEdge()
        setContentView(R.layout.activity_make_route)

        val myLocation = intent.getStringExtra("selfLocation")
        val webView = findViewById<WebView>(R.id.webview)


        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient() // Это предотвратит открытие ссылок в браузер
        webView.loadUrl("http://89.104.66.155/route/${myLocation},47.220878,39.737327")
//        webView.loadUrl("http://10.0.2.2:8000/route/${myLocation},47.220878,39.737327")
    }

}

