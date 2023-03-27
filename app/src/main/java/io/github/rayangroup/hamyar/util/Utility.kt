package io.github.rayangroup.hamyar.util

import android.content.Context
import android.util.Log
import io.github.rayangroup.hamyar.BuildConfig
import java.util.*

fun getCurrentLocale(context: Context): Locale = context.resources.configuration.locales.get(0)

fun String.log() {
    if (BuildConfig.DEBUG)
        Log.d(Constants.LOG_TAG, this)
}