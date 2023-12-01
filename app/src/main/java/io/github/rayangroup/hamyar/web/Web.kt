package io.github.rayangroup.hamyar.web

import io.github.rayangroup.hamyar.web.ApiKey.AUTHORIZATION
import io.github.rayangroup.hamyar.web.ApiKey.KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Web {

    private const val baseUrl = "https://api.openai.com/v1/"

    private const val TIMEOUT = 60L

    private lateinit var retrofit: Retrofit

    private lateinit var okHttpClient: OkHttpClient

    fun getRetrofit(): Retrofit {
        return if (!this::retrofit.isInitialized) {
            okHttpClient = createOkHttpClient()
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        } else retrofit
    }


    private fun createOkHttpClient() = OkHttpClient.Builder()
        .pingInterval(3L, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
        .addInterceptor(HeaderAuthorizationInterceptor(AUTHORIZATION to KEY))
        .build()

    class HeaderAuthorizationInterceptor(
        private val header: Pair<String, String>
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            builder.addHeader(header.first, header.second)
            return chain.proceed(builder.build())
        }
    }

    inline fun <reified T> Retrofit.apiOf(): T = this.create(T::class.java)
}