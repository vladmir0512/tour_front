package com.bazaroff_alexey.newroutes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

class PreferencesActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var highContrastButton: Button
    private lateinit var highContrastText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Получение текущей темы
        val currentTheme = sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // Установка радиокнопки по умолчанию
        val themeRadioGroup = findViewById<RadioGroup>(R.id.themeRadioGroup)
        when (currentTheme) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                themeRadioGroup.check(R.id.radioSystemTheme)
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                themeRadioGroup.check(R.id.radioDarkTheme)
            }
            AppCompatDelegate.MODE_NIGHT_NO -> {
                themeRadioGroup.check(R.id.radioLightTheme)
            }
        }

        // Обработка выбора темы
        themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioSystemTheme -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                R.id.radioDarkTheme -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                R.id.radioLightTheme -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            // Сохранение выбранной темы
            with(sharedPreferences.edit()) {
                putInt("theme", AppCompatDelegate.getDefaultNightMode())
                apply()
            }
        }

        // Настройка для слабовидящих
        highContrastButton = findViewById(R.id.highContrastButton)
        highContrastText = findViewById(R.id.highContrastText)

        // Проверяем состояние режима высокой контрастности
        val highContrastEnabled = sharedPreferences.getBoolean("highContrast", false)
        updateHighContrastMode(highContrastEnabled)

        // Обработка клика на кнопку
        highContrastButton.setOnClickListener {
            // Меняем состояние
            val newHighContrast = !highContrastEnabled
            updateHighContrastMode(newHighContrast)
            // Сохраняем новое состояние
            with(sharedPreferences.edit()) {
                putBoolean("highContrast", newHighContrast)
                apply()
            }
        }
    }

    // Функция для обновления режима высокой контрастности
    private fun updateHighContrastMode(enabled: Boolean) {
        if (enabled) {
            // Включите режим высокой контрастности
            highContrastText.setTextColor(resources.getColor(R.color.black)) // Черный текст
            highContrastText.setBackgroundColor(resources.getColor(R.color.white)) // Белый фон
            //highContrastButton.setText(R.string.disable_high_contrast) // Измените текст кнопки
        } else {
            // Отключите режим высокой контрастности
            highContrastText.setTextColor(resources.getColor(R.color.white)) // Белый текст
            highContrastText.setBackgroundColor(resources.getColor(R.color.black)) // Черный фон
           // highContrastButton.setText(R.string.enable_high_contrast) // Измените текст кнопки
        }
    }
}