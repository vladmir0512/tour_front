package com.bazaroff_alexey.newroutes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class LoginActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        val root_layout: ConstraintLayout = findViewById(R.id.login)
        ViewCompat.setOnApplyWindowInsetsListener(root_layout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val currentTheme = sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO)
        when (currentTheme) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                root_layout.setBackgroundResource(R.drawable.background_dark)
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                root_layout.setBackgroundResource(R.drawable.background_light)
            }
        }
        val login_userLogin = findViewById<EditText>(R.id.login_userLogin)
        val login_userPass = findViewById<EditText>(R.id.login_userPass)
        val txtDontHaveAcc = findViewById<TextView>(R.id.txtDontHaveAcc)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        Utils.highlightText(txtDontHaveAcc, 17, 26)
        onClickListeners(btnLogin, txtDontHaveAcc, login_userLogin, login_userPass)
    }

    private fun onClickListeners(
        btnLogin: Button,
        txtDontHaveAcc: TextView,
        login_userLogin: EditText,
        login_userPass: EditText
    ) {
        btnLogin.setOnClickListener() {
            val email = login_userLogin.text.toString().lowercase(Locale.ROOT).replace(" ", "");
            val password = login_userPass.text.toString().replace(" ", "");
            if (!Utils.validateInput(this, email, password)) {
                return@setOnClickListener
            }
            sendData(email, password)
        }
        txtDontHaveAcc.setOnClickListener() {
            toRegisterActivity()
        }

    }

    private fun sendData(log: String, pass: String) {
        val requestData = RequestData(email = log, password = pass)
        try {
            Log.d("LoginActivity", "Отправка данных: $requestData") // Лог отправляемых данных
            RetrofitAPI.instance.sendLogin(requestData)
                .enqueue(object : Callback<FirebaseAuthResponse> {
                    override fun onResponse(
                        call: Call<FirebaseAuthResponse>,
                        response: Response<FirebaseAuthResponse>
                    ) {
                        if (response.isSuccessful()) {
                            val firebaseAuthResponse = response.body()
                            Log.d(
                                "LoginActivity",
                                "Успешный ответ: $firebaseAuthResponse"
                            )
                            if (firebaseAuthResponse != null) {
                                Log.d("FirebaseAuth", "User  ID: ${firebaseAuthResponse.localId}")
                                Log.d("FirebaseAuth", "Email: ${firebaseAuthResponse.email}")
                                Log.d("FirebaseAuth", "ID Token: ${firebaseAuthResponse.idToken}")
                                Log.d("FirebaseAuth", "ID Token: ${firebaseAuthResponse}")
                                Log.e("FirebaseAuth", firebaseAuthResponse.toString())

                                toLKActivity(firebaseAuthResponse)
                            }
                        } else {
                            val errorResponse = response.errorBody()?.string()
                            Log.e("LoginActivity", "Ошибка: ${response.code()} - $errorResponse")
                            try {
                                val jsonObject = JSONObject(errorResponse)
                                val errorMessage = jsonObject.getString("error")
                                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT)
                                    .show()
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Ошибка: ${response.message()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<FirebaseAuthResponse>, t: Throwable) {
                        Log.e(
                            "LoginActivity",
                            "Ошибка при выполнении запроса: ${t.message}"
                        )
                        Toast.makeText(
                            this@LoginActivity,
                            "Ошибка при выполнении запроса: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                })
        } catch (e: Exception) {
            Log.e("LoginActivity", "Произошла ошибка: ${e.message}") // Лог исключения
            Toast.makeText(this@LoginActivity, "Произошла ошибка: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun toRegisterActivity() {
        val registerActivity = Intent(
            this@LoginActivity,
            RegisterActivity::class.java
        )
        startActivity(registerActivity)
    }

    private fun toLKActivity(firebaseAuthResponse: FirebaseAuthResponse) {
        val lkActivity = Intent(this@LoginActivity, LkActivity::class.java)
        lkActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        lkActivity.putExtra("email", firebaseAuthResponse.email)
        lkActivity.putExtra("user_id", firebaseAuthResponse.localId) // Добавляем user_id!
        lkActivity.putExtra("avatar_url", firebaseAuthResponse.avatarUrl) // Добавляем URL аватара
        saveUidToSharedPreferences(this, firebaseAuthResponse.localId)
        saveEmailToSharedPreferences(this, firebaseAuthResponse.email)
        startActivity(lkActivity)
        finish()
    }

}

fun saveUidToSharedPreferences(context: Context, uid: String) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.putString("firebaseAuthRes", uid)
    editor.apply()
}

fun saveEmailToSharedPreferences(context: Context, email: String) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("userEmail", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.putString("firebaseEmail", email)
    editor.apply()
}