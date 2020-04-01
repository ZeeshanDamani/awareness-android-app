package com.corona.awareness.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitConnection {

    private var BASE_URL: String = "http://34.245.192.132:8011"
    private val PACKAGE: String = "/api/v1/"
    private lateinit var client: OkHttpClient

    fun getAPIClient(authKey: String): APIInterface {

//        client = OkHttpClient().newBuilder()
//            .addInterceptor(
//                HttpLoggingInterceptor()
//                    .setLevel(HttpLoggingInterceptor.Level.BODY)
//            )
//            .build()

        client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + authKey).build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL + PACKAGE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(APIInterface::class.java)

    }


}