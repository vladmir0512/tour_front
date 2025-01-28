package com.bazaroff_alexey.newroutes

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast

object Utils {
    fun highlightText(word: TextView, startSymbol: Int, endSymbol: Int){
        val spannableStringBuilder = SpannableStringBuilder()

        spannableStringBuilder.append(word.text);
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(Color.WHITE),
            startSymbol,
            endSymbol,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        word.text = spannableStringBuilder;
    }

    fun validateInput(context: Context, email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return false
        }

        // Проверка формата электронной почты
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Введите корректный адрес электронной почты", Toast.LENGTH_SHORT).show()
            return false
        }

        // Проверка длины пароля
        if (password.length < 6) {
            Toast.makeText(context, "Пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun showExitConfirmationDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Вы действительно хотите выйти?")
        builder.setPositiveButton("Да") { _, _ ->
            // Закрываем приложение
            Toast.makeText(context, "Спасибо, что вы с нами!", Toast.LENGTH_SHORT).show()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)

        }
        builder.setNegativeButton("Нет") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.isEnabled = false // Изначально "Да" не активна
        positiveButton.setBackgroundColor(context.resources.getColor(R.color.grey)) // Серый цвет

        Handler(Looper.getMainLooper()).postDelayed({
            positiveButton.isEnabled = true // Делаем "Да" активной через 5 секунд
            positiveButton.setBackgroundColor(context.resources.getColor(R.color.red)) // Красный цвет
        }, 5000)
    }




}