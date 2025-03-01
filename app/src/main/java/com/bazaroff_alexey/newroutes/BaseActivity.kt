package com.bazaroff_alexey.newroutes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

open class BaseActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        //applyFontScale()
        //applyTheme()


    }

    private fun applyTheme() {

        try {
            // Ваш код, например, чтение данных из SharedPreferences
            val theme =
                sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            AppCompatDelegate.setDefaultNightMode(theme)
        } catch (e: Exception) {
            Log.e("BaseActivity", "Ошибка при применении темы: ${e.message}")
        }
    }

    fun applyFontScale() {
        try {
            val fontScale = sharedPreferences.getFloat("font_scale", 1.0f)
            val configuration = resources.configuration
            configuration.fontScale = fontScale
            val metrics = resources.displayMetrics
            resources.updateConfiguration(configuration, metrics)
        } catch (e: Exception) {
            Log.e("BaseActivity", "Ошибка при применении шрифта: ${e.message}")
        }


    }
}
