package com.bazaroff_alexey.newroutes

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.Toast

class PrivacyPolicyActivity : BaseActivity() {
    private lateinit var btnAcknowledged: Button
    private lateinit var scrollView: ScrollView
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        scrollView = findViewById(R.id.SVokPP)
        btnAcknowledged =
            findViewById(R.id.btnAcknowledged)
        btnAcknowledged.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
        Toast.makeText(this, "Поздравляем с ознакомлением!", Toast.LENGTH_SHORT).show()
    }
}