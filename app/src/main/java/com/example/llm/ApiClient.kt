package com.example.llm.network

import com.example.llm.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->

        val request = chain.request()
            .newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${Constants.OPENROUTER_API_KEY}"
            )
            .addHeader(
                "HTTP-Referer",
                "https://example.com"
            )
            .addHeader(
                "X-Title",
                "LLM Evaluation"
            )
            .build()

        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(
            Constants.CONNECT_TIMEOUT,
            TimeUnit.SECONDS
        )
        .readTimeout(
            Constants.READ_TIMEOUT,
            TimeUnit.SECONDS
        )
        .addInterceptor(authInterceptor)
        .addInterceptor(logger)
        .build()

    val api: OpenRouterApi by lazy {

        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(OpenRouterApi::class.java)
    }
}