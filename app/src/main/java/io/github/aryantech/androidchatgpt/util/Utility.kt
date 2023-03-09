package io.github.aryantech.androidchatgpt.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.util.*

@Suppress("DEPRECATION")
fun getCurrentLocale(context: Context): Locale = context.resources.configuration.locales.get(0)

@SuppressLint("LogConditional")
fun String.log() {
    Log.d(Constants.LOG_TAG, this)
}