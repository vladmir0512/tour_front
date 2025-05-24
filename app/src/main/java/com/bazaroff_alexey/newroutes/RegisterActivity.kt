package com.bazaroff_alexey.newroutes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class RegisterActivity : BaseActivity() {
    private lateinit var checkboxPrivacyPolicy: CheckBox
    private lateinit var checkboxDataProcessing: CheckBox

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        Utils.clearUidFromSharedPreferences(this)

        val root_layout: ConstraintLayout = findViewById(R.id.main)
        val currentTheme = sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO)
        when (currentTheme) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                root_layout.setBackgroundResource(R.drawable.background_dark)
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                root_layout.setBackgroundResource(R.drawable.background_light)
            }
        }
        checkboxPrivacyPolicy = findViewById<CheckBox>(R.id.checkbox_privacy_policy)
        checkboxDataProcessing = findViewById<CheckBox>(R.id.checkbox_data_processing)
        val userEmail: EditText = findViewById<EditText>(R.id.userEmail)
        val userPass: EditText = findViewById<EditText>(R.id.userPass)
        val txtHaveAcc: TextView = findViewById<TextView>(R.id.txtHaveAcc)
        val btnReg: Button = findViewById<Button>(R.id.btnReg)
        btnReg.isEnabled = false
        val checkChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                val isPrivacyChecked: Boolean = checkboxPrivacyPolicy.isChecked()
                val isDataProcessingChecked: Boolean = checkboxDataProcessing.isChecked()
                btnReg.setEnabled(isPrivacyChecked && isDataProcessingChecked)
            }
        checkboxPrivacyPolicy.setOnCheckedChangeListener(checkChangeListener)
        checkboxDataProcessing.setOnCheckedChangeListener(checkChangeListener)
        checkboxPrivacyPolicy.setOnClickListener {
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PRIVACY)
        }
        Utils.highlightText(txtHaveAcc, 18, 24)
        onClickListeners(btnReg, txtHaveAcc, userEmail, userPass, checkboxPrivacyPolicy)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PRIVACY && resultCode == RESULT_OK) {
            checkboxPrivacyPolicy.isChecked = true
        }
    }

    private fun onClickListeners(
        btnReg: Button,
        txtHaveAcc: TextView,
        userEmail: EditText,
        userPass: EditText,
        checkboxPrivacyPolicy: CheckBox
    ) {
        btnReg.setOnClickListener() {
            sendData(userEmail, userPass)
        }
        txtHaveAcc.setOnClickListener() {
            toLoginActivity()
        }
    }

    private fun sendData(userEmail: EditText, userPass: EditText) {
        val email = userEmail.text.toString().lowercase(Locale.ROOT).replace(" ", "");
        val password = userPass.text.toString().replace(" ", "");
        if (!Utils.validateInput(this, email, password)) {
            return
        }
        val requestData = RequestData(email = email, password = password)
        try {
            Log.d("RegisterActivity", "Отправка данных: $requestData")
            RetrofitAPI.instance.sendRegister(requestData).enqueue(object : Callback<ResponseData> {
                override fun onResponse(
                    call: Call<ResponseData>,
                    response: Response<ResponseData>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        Log.d(
                            "RegisterActivity",
                            "Успешный ответ: $responseData"
                        )
                        Toast.makeText(
                            this@RegisterActivity,
                            responseData?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        toLoginActivity()
                    } else {
                        if (response.code() == 400) {
                            Log.e(
                                "RegisterActivity",
                                "Ошибка: ${response.code()} Пользователь с такой почтой уже существует"
                            )
                            Toast.makeText(
                                this@RegisterActivity,
                                "Пользователь с такой почтой уже существует",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Log.e("RegisterActivity", "Ошибка: ${response.code()}")
                            Toast.makeText(
                                this@RegisterActivity,
                                "Ошибка: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    Log.e(
                        "RegisterActivty",
                        "Ошибка при выполнении запроса: ${t.message}"
                    )
                    Toast.makeText(
                        this@RegisterActivity,
                        "Ошибка: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } catch (e: Exception) {
            Log.e("RegisterActivity", "Произошла ошибка: ${e.message}") // Лог исключения
            Toast.makeText(
                this@RegisterActivity,
                "Произошла ошибка: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun toLoginActivity() {
        val loginActivity = Intent(this@RegisterActivity, LoginActivity::class.java)
        loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(loginActivity)
        finish()
    }

    private fun toPrivacyPolicy() {
        val privacyPolicy = Intent(this@RegisterActivity, PrivacyPolicyActivity::class.java)
        startActivity(privacyPolicy)
    }

    companion object {
        private const val REQUEST_CODE_PRIVACY = 1
    }
}







