package com.corona.awareness.network

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


object RetrofitConnection {

    private const val BASE_URL: String = "http://34.245.192.132:8012"
    private const val PACKAGE: String = "/api/v1/"

    private val errorInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val httpResponse = chain.proceed(chain.request())
            if (httpResponse.isSuccessful) {
                return httpResponse
            }

            throw BackendException(httpResponse.code(), httpResponse.body()?.string() ?: "")
        }
    }

    fun getAPIClient(): APIInterface {
        return getAPIClient(getOkHttpClient())
    }

    fun getAPIClient(authKey: String): APIInterface {

        val client = getOkHttpClient().newBuilder()
            .addInterceptor(errorInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + authKey).build()
                chain.proceed(request)
            }
            .build()

        return getAPIClient(client)
    }

    private fun getAPIClient(okHttpClient: OkHttpClient): APIInterface {
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL + PACKAGE)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(APIInterface::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }
}

class BackendException(code: Int, body: String) :
    IOException("Request failed with status code $code, message: $body")