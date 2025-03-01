package com.bazaroff_alexey.newroutes

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class PreferencesActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fontSizeButton: Button
    private lateinit var radioLightTheme: RadioButton
    private lateinit var radioDarkTheme: RadioButton
    private lateinit var themeRadioGroup: RadioGroup

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Устанавливаем тему перед setContentView
        applyTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        // Инициализация UI элементов
        fontSizeButton = findViewById(R.id.fontSizeButton)
        radioLightTheme = findViewById(R.id.radioLightTheme)
        radioDarkTheme = findViewById(R.id.radioDarkTheme)
        themeRadioGroup = findViewById(R.id.themeRadioGroup)

        // Загружаем текущую тему в UI
        val currentTheme =
            sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (currentTheme) {
            AppCompatDelegate.MODE_NIGHT_YES -> radioDarkTheme.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> radioLightTheme.isChecked = true
        }

        // Обработчик выбора темы
        themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioLightTheme -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPreferences.edit().putInt("theme", AppCompatDelegate.MODE_NIGHT_NO)
                        .apply()
                }

                R.id.radioDarkTheme -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPreferences.edit().putInt("theme", AppCompatDelegate.MODE_NIGHT_YES)
                        .apply()
                }
            }
            recreate() // Перезапуск активности для применения новой темы
        }

        // Изменение размера шрифта
        val isLargeText = sharedPreferences.getBoolean("largeText", false)
        updateFontSizeButtonText(isLargeText)
        fontSizeButton.setOnClickListener {
            val newSize = !sharedPreferences.getBoolean("largeText", false)
            sharedPreferences.edit().putBoolean("largeText", newSize).apply()
            recreate() // Перезапуск активности
        }
    }

    private fun applyTheme() {
        val theme = sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(theme) // Устанавливаем тему
    }

    private fun updateFontSizeButtonText(isLargeText: Boolean) {
        fontSizeButton.text = if (isLargeText) {
            "Уменьшить шрифт"
        } else {
            "Увеличить шрифт"
        }
    }
}
