package com.bazaroff_alexey.newroutes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate

class PreferencesActivity : BaseActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fontSizeButton: Button
    private lateinit var radioLightTheme: RadioButton
    private lateinit var radioDarkTheme: RadioButton
    private lateinit var themeRadioGroup: RadioGroup
    private lateinit var txtToLk: TextView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val userId = intent?.getStringExtra("uid")
        if (userId.isNullOrEmpty()) {
            Log.e("PreferencesActivity", "Ошибка: UID не передан!")
        } else {
            Log.d("PreferencesActivity", "Получен UID: $userId")
        }

        val isLargeText = sharedPreferences.getFloat("font_scale", 1.0f) > 1.0f
        fontSizeButton = findViewById(R.id.fontSizeButton)
        updateFontSizeButtonText(isLargeText)


        // Инициализация UI элементов
        radioLightTheme = findViewById(R.id.radioLightTheme)
        radioDarkTheme = findViewById(R.id.radioDarkTheme)
        themeRadioGroup = findViewById(R.id.themeRadioGroup)
        txtToLk = findViewById(R.id.txtToLk)

        // Обработчик нажатия кнопки изменения размера шрифта
        fontSizeButton.setOnClickListener {
            toggleFontSize()
            recreate()
        }

        txtToLk.setOnClickListener {
            val lkActivity = Intent(this, LkActivity::class.java)
            lkActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(lkActivity)
        }
        // Загружаем текущую тему в UI

        themeRadioGroup.setOnCheckedChangeListener(null) // Отключаем слушатель

        val currentTheme = sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO)
        when (currentTheme) {
            AppCompatDelegate.MODE_NIGHT_YES -> radioDarkTheme.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> radioLightTheme.isChecked = true
        }

        themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val newTheme = when (checkedId) {
                R.id.radioDarkTheme -> AppCompatDelegate.MODE_NIGHT_YES
                R.id.radioLightTheme -> AppCompatDelegate.MODE_NIGHT_NO
                else -> return@setOnCheckedChangeListener
            }

            sharedPreferences.edit().putInt("theme", newTheme).apply()
            AppCompatDelegate.setDefaultNightMode(newTheme)
        }

    }


    override fun onBackPressed() {
        // Просто ничего не делаем, чтобы запретить действие кнопки "Назад"
    }


    private fun updateFontSizeButtonText(isLargeText: Boolean) {
        fontSizeButton.text = if (isLargeText) {
            "Уменьшить шрифт"
        } else {
            "Увеличить шрифт"
        }
    }

    private fun toggleFontSize() {
        val currentScale = sharedPreferences.getFloat("font_scale", 1.0f)
        val newScale = if (currentScale == 1.0f) 1.2f else 1.0f

        sharedPreferences.edit().putFloat("font_scale", newScale).apply()

        // Обновляем текст кнопки
        updateFontSizeButtonText(newScale > 1.0f)

        // Применяем новый масштаб и перезапускаем активность
        applyFontScale()
    }


}