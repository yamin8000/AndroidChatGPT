package io.github.rayangroup.hamyar.util

import io.github.rayangroup.hamyar.db.AppDatabase

object Constants {
    lateinit var db: AppDatabase
    fun isDbInitialized() = ::db.isInitialized
    const val LOG_TAG = "<==>"

    const val THEME = "theme"

    const val API_MODEL = "api_model"
    const val API_MODELS = "api_models"

    const val DEFAULT_LANGUAGE_TAGS = "fa,en"

    const val DEFAULT_API_MODEL = "gpt-3.5-turbo"

    val CHAT_MODELS = listOf("gpt-3.5-turbo", "gpt-3.5-turbo-0301")

    val EDIT_MODELS = listOf("text-davinci-edit-001", "code-davinci-edit-001")

    val PERSIAN_REGEX = Regex("[\\u0621-\\u064a]+")

    const val TITLE_PREDICTION_PROMPT = "find a title for this conversion:\n"

    const val INTERNET_CHECK_DELAY = 3000L
    val DNS_SERVERS = listOf(
        "8.8.8.8",
        "8.8.4.4",
        "1.1.1.1",
        "1.0.0.1",
        "185.51.200.2",
        "178.22.122.100",
        "10.202.10.202",
        "10.202.10.102"
    )
}