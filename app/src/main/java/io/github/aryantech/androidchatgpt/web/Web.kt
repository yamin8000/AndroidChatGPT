package io.github.aryantech.androidchatgpt.web

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration
import java.util.concurrent.TimeUnit

object Web {

    private const val baseUrl = "https://api.openai.com/v1/"

    private lateinit var retrofit: Retrofit

    fun getRetrofit(): Retrofit {
        return if (!this::retrofit.isInitialized) {
            val okHttpClient = createOkHttpClient()
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        } else retrofit
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor {
                it.proceed(
                    it.request()
                        .newBuilder()
                        .addHeader(ApiKey.AUTHORIZATION, ApiKey.KEY)
                        .build()
                )
            }.build()
    }

    inline fun <reified T> Retrofit.apiOf(): T = this.create(T::class.java)
}