package com.bazaroff_alexey.newroutes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale




class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Указываем переменные, введенные пользователем из EditText
        val userEmail: EditText = findViewById<EditText>(R.id.userEmail)
        val userPass: EditText = findViewById<EditText>(R.id.userPass)
        // Кнопки
        val txtHaveAcc: TextView = findViewById<TextView>(R.id.txtHaveAcc)
        val btnReg: Button = findViewById<Button>(R.id.btnReg)

        // "Войти" будет другим цветом
        Utils.highlightText(txtHaveAcc, 18, 24)

        onClickListeners(btnReg, txtHaveAcc, userEmail, userPass)
    }

    private fun onClickListeners(btnReg: Button, txtHaveAcc: TextView, userEmail: EditText, userPass: EditText){

        btnReg.setOnClickListener() {
            sendData(userEmail, userPass)
        }

        txtHaveAcc.setOnClickListener() {
            toLoginActivity()
        }
    }
    private fun sendData(userEmail: EditText, userPass: EditText){
        // Валидация полей
        val email = userEmail.text.toString().lowercase(Locale.ROOT).replace(" ", "");
        val password = userPass.text.toString().replace(" ", "");
        if (!Utils.validateInput(this, email, password)) {
            return
        }
        // Формируем запрос
        val requestData = RequestData(email = email, password = password)
        try {
            // Отправляем данные
            Log.d("RegisterActivity", "Отправка данных: $requestData") // Лог отправляемых данных
            RetrofitAPI.instance.sendRegister(requestData).enqueue(object : Callback<ResponseData> {
                override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        Log.d("RegisterActivity", "Успешный ответ: $responseData") // Лог успешного ответа
                        Toast.makeText(this@RegisterActivity, responseData?.message, Toast.LENGTH_SHORT).show()
                        toLoginActivity()
                    } else {
                        Log.e("RegisterActivity", "Ошибка: ${response.code()}") // Лог ошибки
                        Toast.makeText(this@RegisterActivity, "Ошибка: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    Log.e("RegisterActivty", "Ошибка при выполнении запроса: ${t.message}") // Лог ошибки при выполнении запроса
                    Toast.makeText(this@RegisterActivity, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            Log.e("RegisterActivity", "Произошла ошибка: ${e.message}") // Лог исключения
            Toast.makeText(this@RegisterActivity, "Произошла ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun toLoginActivity()
    {
        // Скачок на LoginActivity
        val loginActivity = Intent(this@RegisterActivity, LoginActivity::class.java)
        loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(loginActivity)
        finish()
    }

}









