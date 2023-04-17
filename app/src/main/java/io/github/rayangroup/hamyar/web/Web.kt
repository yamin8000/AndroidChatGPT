package io.github.rayangroup.hamyar.web

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

    private lateinit var logOkHttp: OkHttpClient

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

    fun getLogOkHttp(): OkHttpClient {
        return if (!this::logOkHttp.isInitialized) {
            return OkHttpClient.Builder()
                .addInterceptor(
                    HeaderAuthorizationInterceptor(
                        ApiKey.AUTHORIZATION, "Bearer ${ApiKey.GITHUB_TOKEN}"
                    )
                )
                .build()
        } else logOkHttp
    }

    private fun createOkHttpClient() = OkHttpClient.Builder()
        .pingInterval(3L, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(HeaderAuthorizationInterceptor(ApiKey.AUTHORIZATION, ApiKey.KEY))
        .protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
        .build()

    class HeaderAuthorizationInterceptor(
        private val headers: Map<String, String>
    ) : Interceptor {
        constructor(
            header: String,
            value: String
        ) : this(mapOf(header to value))

        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            headers.forEach { (header, value) ->
                builder.addHeader(header, value)
            }
            return chain.proceed(builder.build())
        }
    }

    inline fun <reified T> Retrofit.apiOf(): T = this.create(T::class.java)
}