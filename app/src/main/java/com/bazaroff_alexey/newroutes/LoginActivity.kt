package com.bazaroff_alexey.newroutes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Указываем переменные, введенные пользователем из EditText
        // Поля ввода
        val login_userLogin = findViewById<EditText>(R.id.login_userLogin)
        val login_userPass = findViewById<EditText>(R.id.login_userPass)

        // Кнопки
        val txtDontHaveAcc = findViewById<TextView>(R.id.txtDontHaveAcc)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

    // Листенеры - в бой!
        txtDontHaveAcc.setOnClickListener() {
            // Скачок на RegisterActivity
            val registerActivity = Intent(
                this,
                RegisterActivity::class.java
            )
            startActivity(registerActivity)
        }

        btnLogin.setOnClickListener() {
            val lkActivity = Intent(this, LkActivity::class.java)
            lkActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(lkActivity)
            finish();
        }

    }


    }


