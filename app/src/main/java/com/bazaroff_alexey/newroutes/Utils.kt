package com.bazaroff_alexey.newroutes

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import android.widget.Toast


object Utils {
    fun highlightText(word: TextView, startSymbol: Int, endSymbol: Int) {
        val spannableStringBuilder = SpannableStringBuilder()
        spannableStringBuilder.append(word.text);
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(Color.GRAY),
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
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(
                context,
                "Введите корректный адрес электронной почты",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(
                context,
                "Пароль должен содержать не менее 6 символов",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    fun showExitConfirmationDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Вы действительно хотите выйти?")
        builder.setPositiveButton("Да") { _, _ ->
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
        positiveButton.isEnabled = false
        positiveButton.setBackgroundColor(context.resources.getColor(R.color.grey))
        Handler(Looper.getMainLooper()).postDelayed({
            positiveButton.isEnabled = true
            positiveButton.setBackgroundColor(context.resources.getColor(R.color.red))
        }, 5000)
    }

    fun getUidFromSharedPreferences(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(
            "firebaseAuthRes",
            null
        )
    }

    fun getEmailFromSharedPreferences(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("userEmail", Context.MODE_PRIVATE)
        return sharedPreferences.getString(
            "firebaseEmail",
            null
        )
    }

    fun clearUidFromSharedPreferences(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove("UID")
        editor.apply()
    }
}