package io.github.aryantech.androidchatgpt.util

import io.github.aryantech.androidchatgpt.db.AppDatabase

object Constants {
    lateinit var db: AppDatabase
    fun isDbInitialized() = ::db.isInitialized

    const val LOG_TAG = "<==>"
    const val THEME = "theme"

    const val API_MODEL = "api_model"
    const val API_MODELS = "api_models"

    const val DEFAULT_API_MODEL = "gpt-3.5-turbo"
    val CHAT_MODELS = listOf("gpt-3.5-turbo", "gpt-3.5-turbo-0301")

    val PERSIAN_REGEX = Regex("""([\u0621-\u0659]+|[\u0670-\u06cc]+)+""")

    const val INTERNET_CHECK_DELAY = 3000L
    val DNS_SERVERS = listOf("8.8.8.8", "8.8.4.4", "1.1.1.1", "4.2.2.4")
}