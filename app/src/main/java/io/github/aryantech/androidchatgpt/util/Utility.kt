package io.github.aryantech.androidchatgpt.util

import android.content.Context
import android.util.Log
import io.github.aryantech.androidchatgpt.BuildConfig
import java.util.*

fun getCurrentLocale(context: Context): Locale = context.resources.configuration.locales.get(0)

fun String.log() {
    if (BuildConfig.DEBUG)
        Log.d(Constants.LOG_TAG, this)
}