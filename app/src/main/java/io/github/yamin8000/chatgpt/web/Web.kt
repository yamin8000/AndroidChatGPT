/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     Web.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     Web.kt Last modified at 2023/12/1
 *     This file is part of AndroidChatGPT/AndroidChatGPT.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     AndroidChatGPT/AndroidChatGPT.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     AndroidChatGPT/AndroidChatGPT.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with AndroidChatGPT.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.chatgpt.web

import io.github.yamin8000.chatgpt.web.ApiKey.AUTHORIZATION
import io.github.yamin8000.chatgpt.web.ApiKey.KEY
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