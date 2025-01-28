package com.bazaroff_alexey.newroutes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lk)

        // Указываем переменные, введенные пользователем из EditText
            // Поля ввода
            val userEmail = intent.getStringExtra("email") // Используйте тот же ключ, что и в LoginActivity

            // Кнопки
            val exit = findViewById<Button>(R.id.exit);
            val makeRoute = findViewById<Button>(R.id.makeRoute);
            val historyRoute = findViewById<Button>(R.id.historyRoute);
            val preference = findViewById<Button>(R.id.preference);
            val tech = findViewById<Button>(R.id.tech);
            val contacts = findViewById<Button>(R.id.contacts);
            val avatar = findViewById<ImageView>(R.id.avatar);
            val privacyPolicy = findViewById<ImageView>(R.id.privacyPolicy);

            val titleLkActivity = findViewById<TextView>(R.id.titleLk)

            titleLkActivity.text = "Личный кабинет\n$userEmail"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lk)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        exit.setOnClickListener(){
//            // Выход
//            Toast.makeText(this, "Спасибо, что вы с нами!", Toast.LENGTH_SHORT).show()
//            finish()
              Utils.showExitConfirmationDialog(this)
        }
        makeRoute.setOnClickListener(){
            // Скачок на HistoryRoutes
            val makeRouteActivity = Intent(this, MakeRouteActivity::class.java)
            startActivity(makeRouteActivity)
        }
        historyRoute.setOnClickListener(){
            // Скачок на HistoryRoutes
            val historyRoutes = Intent(this, HistoryRoutesActivity::class.java)
            startActivity(historyRoutes)
        }
        preference.setOnClickListener(){
            // Скачок на MekeRoute
            val preferenceActivity = Intent(this, PreferencesActivity::class.java)
            startActivity(preferenceActivity)
        }
        tech.setOnClickListener(){
            Toast.makeText(this, "Техническая поддержка.", Toast.LENGTH_SHORT).show();

            // Скачок на MekeRoute
            //val techSupportActivity = Intent(this, TechSupportActivity::class.java)
            //startActivity(techSupportActivity)
        }
        contacts.setOnClickListener(){
            // Скачок на MekeRoute
            val contactsActivity = Intent(this, ContactsActivity::class.java)
            startActivity(contactsActivity)
        }
        avatar.setOnClickListener(){
            Toast.makeText(this, "Тут место для вашего аватара.", Toast.LENGTH_SHORT).show()
        }
        privacyPolicy.setOnClickListener(){
            Toast.makeText(this, "Политика конфиденциальности.", Toast.LENGTH_SHORT).show()
            val privacyPolicyActivity = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(privacyPolicyActivity)
        }
    }


    }
