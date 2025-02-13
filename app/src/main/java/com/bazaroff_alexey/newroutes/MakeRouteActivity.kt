package com.bazaroff_alexey.newroutes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class MakeRouteActivity : AppCompatActivity() {


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Intent", "Попали в MakeRouteActivity")

//      enableEdgeToEdge()
        setContentView(R.layout.activity_make_route)

        val myLocation = intent.getStringExtra("selfLocation")
        val finLocation = intent.getStringExtra("finLocation")
        Log.d("Intent", "myLocation: ${myLocation}")
        Log.d("Intent", "finLocation: ${finLocation}")
        Log.d("Intent", "http://10.0.2.2:8000/route/${myLocation},${finLocation}")

        val webView = findViewById<WebView>(R.id.webview)


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().useWideViewPort = true;
        webView.getSettings().setDomStorageEnabled(true);

        webView.webViewClient = WebViewClient() // Это предотвратит открытие ссылок в браузер
//        webView.loadUrl("http://89.104.66.155/route/${myLocation},${finLocation}")
        webView.loadUrl("http://10.0.2.2:8000/route/${myLocation},${finLocation}")
    }

}

