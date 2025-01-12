package com.bazaroff_alexey.newroutes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Указываем переменные, введенные пользователем из EditText
        // Поля ввода
        val userLogin = findViewById<EditText>(R.id.userLogin)
        val userEmail = findViewById<EditText>(R.id.userEmail)
        val userPass = findViewById<EditText>(R.id.userPass)

        // Кнопки
        val txtHaveAcc = findViewById<TextView>(R.id.txtHaveAcc)
        val btnReg = findViewById<Button>(R.id.btnReg)
        val spannableStringBuilder = SpannableStringBuilder()

        spannableStringBuilder.append(txtHaveAcc.text);
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.WHITE), 18, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // "Hello" будет красным

        txtHaveAcc.text = spannableStringBuilder;


        txtHaveAcc.setOnClickListener(){
            // Скачок на LoginActivity
            val loginActivity = Intent(this, LoginActivity::class.java)
            loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(loginActivity)
            finish()

        }

        btnReg.setOnClickListener(){
            Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()        }
        }

}