// PrivacyPolicyActivity.kt
package com.bazaroff_alexey.newroutes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PrivacyPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        val SVokpp: ScrollView = findViewById<ScrollView>(R.id.SVokPP)
        Toast.makeText(this, "Поздравляем с ознакомлением!", Toast.LENGTH_SHORT).show()

        // Устанавливаем слушатель прокрутки
        SVokpp.viewTreeObserver.addOnScrollChangedListener {
            // Проверяем, прокручен ли ScrollView до конца
            if (SVokpp.getChildAt(0).bottom <= (SVokpp.height + SVokpp.scrollY)) {
                // Показываем кнопку "ОК" через 5 секунд
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 3000) // 3000 миллисекунд = 3 секунды

            }

        }
    }
}