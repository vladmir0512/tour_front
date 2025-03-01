package com.bazaroff_alexey.newroutes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class LkActivity : BaseActivity() {

    private lateinit var avatarImageView: ImageView
    private lateinit var emailTextView: TextView
    private lateinit var userId: String
    private val PICK_IMAGE_REQUEST = 1


    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lk)
        // Указываем переменные, введенные пользователем из EditText
        // Поля ввода


        val userId = Utils.getUidFromSharedPreferences(this)

        if (userId != null) {
            // используем UID
            Log.d("LkActivity", "UID: $userId")
        } else {
            // Если UID не найден, например, перенаправляем на экран логина
            Toast.makeText(this, "Пожалуйста, войдите снова", Toast.LENGTH_SHORT).show()
        }

        val userEmail = Utils.getEmailFromSharedPreferences(this)
        // Кнопки
        val exit = findViewById<Button>(R.id.exit);
        val makeRoute = findViewById<Button>(R.id.makeRoute);
        val historyRoute = findViewById<Button>(R.id.historyRoute);
        val preference = findViewById<Button>(R.id.preferenceId);
        val tech = findViewById<Button>(R.id.tech);
        val contacts = findViewById<Button>(R.id.contacts);
        val privacyPolicy = findViewById<ImageView>(R.id.privacyPolicy);

        val titleLkActivity = findViewById<TextView>(R.id.titleLk)

        titleLkActivity.text = "Личный кабинет\n$userEmail"
        avatarImageView = findViewById(R.id.avatar)
        emailTextView = findViewById(R.id.titleLk)

        emailTextView.text =
            intent.getStringExtra("email") ?: Utils.getEmailFromSharedPreferences(this)




        exit.setOnClickListener() {
//            // Выход
            Utils.showExitConfirmationDialog(this)
        }
        makeRoute.setOnClickListener() {
            Log.d("LkActivity", "Передача userId: $userId")

            val addressActivity = Intent(this, AddressActivity::class.java)
            addressActivity.putExtra("USER_ID", userId) // убедись, что userId - это Int
            startActivity(addressActivity)

        }
        historyRoute.setOnClickListener() {
            // Скачок на HistoryRoutes
            val historyRoutes = Intent(this, HistoryRoutesActivity::class.java)
            startActivity(historyRoutes)
        }
        preference.setOnClickListener() {

            if (userId.isNullOrEmpty()) {
                Log.e("LkActivity", "Ошибка: uid пустой!")
            } else {
                Log.d("LkActivity", "Передача uid: $userId в PreferencesActivity")
                val preferenceActivity = Intent(this, PreferencesActivity::class.java)
                preferenceActivity.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                preferenceActivity.putExtra("userId", userId)
                startActivity(preferenceActivity)
            }

        }
        tech.setOnClickListener() {
            Toast.makeText(this, "Техническая поддержка.", Toast.LENGTH_SHORT).show();

            // Скачок на MekeRoute
            //val techSupportActivity = Intent(this, TechSupportActivity::class.java)
            //startActivity(techSupportActivity)
        }
        contacts.setOnClickListener() {
            // Скачок на MekeRoute
            val contactsActivity = Intent(this, ContactsActivity::class.java)
            startActivity(contactsActivity)
        }
        Log.d("LkActivity", "USER_ID: $userId")

        if (userId != null) {
            loadUserAvatar(userId)
        } else {
            Log.d("LkActivity", "userId: ${userId}")
        }

        avatarImageView.setOnClickListener {
            requestPermissionAndOpenGallery()
        }
    }

    private fun updateAvatarUI(avatarUrl: String?) {
        Log.d("LkActivity", "Final Avatar URL: $avatarUrl") // Проверяем URL

        Glide.with(this)
            .load(avatarUrl)
            .placeholder(R.drawable.placeholder_avatar)
            .error(R.mipmap.avatar)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(avatarImageView)
    }


    private fun loadUserAvatar(userId: String) {
        RetrofitAPI.instance.getUserAvatar(userId)
            .enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        updateAvatarUI(response.body()?.avatarUrl)
                    } else {
                        Log.e(
                            "LkActivity",
                            "Ошибка загрузки аватара: ${response.errorBody()?.string()}"
                        )
                        updateAvatarUI(null)
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Log.e("LkActivity", "Ошибка загрузки аватара: ${t.message}")
                    updateAvatarUI(null)
                }
            })
    }

    private fun getImagePermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun requestPermissionAndOpenGallery() {
        val permission = getImagePermission()
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openGallery()
        } else {
            Toast.makeText(this, "Разрешение отклонено!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                val imageFile = uriToFile(it)
                if (imageFile != null) {
                    uploadAvatar(imageFile, userId)
                } else {
                    Toast.makeText(this, "Ошибка получения файла", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val file = File(cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
            inputStream.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            Log.e("LkActivity", "Ошибка при конвертации URI в файл: ${e.localizedMessage}")
            null
        }
    }

    private fun uploadAvatar(imageFile: File, userId: String) {
        val requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
        val body = MultipartBody.Part.createFormData("avatar", imageFile.name, requestFile)
        val userIdBody = RequestBody.create(MediaType.parse("text/plain"), userId)

        RetrofitAPI.instance.uploadAvatar(body, userIdBody)
            .enqueue(object : Callback<UploadAvatarResponse> {
                override fun onResponse(
                    call: Call<UploadAvatarResponse>,
                    response: Response<UploadAvatarResponse>
                ) {
                    if (response.isSuccessful) {
                        updateAvatarUI(response.body()?.avatarUrl)
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Неизвестная ошибка"
                        Log.e("AvatarUpload", "Ошибка загрузки: $errorMessage")
                        Toast.makeText(this@LkActivity, "Ошибка: $errorMessage", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<UploadAvatarResponse>, t: Throwable) {
                    Log.e("AvatarUpload", "Ошибка загрузки аватара: ${t.message}")
                }
            })
    }

//    override fun onResume() {
//        super.onResume()
//        val updateTheme = intent.getBooleanExtra("updateTheme", false)
//
//        if (updateTheme) {
//            intent.putExtra("updateTheme", false) // Сбрасываем флаг перед recreate()
//            recreate()
//        }
//    }

}