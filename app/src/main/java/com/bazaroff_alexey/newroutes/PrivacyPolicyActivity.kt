package com.bazaroff_alexey.newroutes

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var btnAcknowledged: Button
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isLargeText = sharedPreferences.getBoolean("largeText", false)
        setTheme(if (isLargeText) R.style.LargeFontTheme else R.style.NormalFontTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        scrollView = findViewById(R.id.SVokPP)
        btnAcknowledged =
            findViewById(R.id.btnAcknowledged) // Предполагается, что вы добавите эту кнопку в ваш XML

        btnAcknowledged.setOnClickListener {
            setResult(RESULT_OK)
            finish() // Закрываем активность и передаём результат
        }
        Toast.makeText(this, "Поздравляем с ознакомлением!", Toast.LENGTH_SHORT).show()

        
    }
}