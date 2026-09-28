package com.example.satupilah.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // IP 10.0.2.2 menunjuk ke localhost komputer saat dijalankan di Android Emulator (Laragon).
    // Jika menggunakan perangkat fisik (USB Debugging/WiFi), ganti dengan IP laptop Anda (misal: "http://192.168.1.15/satupilah_api/")
    private const val BASE_URL = "http://10.0.2.2/satupilah_api/"

    // Logging Interceptor untuk menampilkan log HTTP Request & Response di Logcat Android Studio
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient dengan batas waktu timeout
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Instansi ApiService Retrofit yang siap digunakan di Repository
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}