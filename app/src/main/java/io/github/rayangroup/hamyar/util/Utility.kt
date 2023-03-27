package io.github.rayangroup.hamyar.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import io.github.rayangroup.hamyar.BuildConfig
import java.util.*


fun Context.isLocalePersian(text: String): Boolean {
    val currentLocale = getCurrentLocale(this).language
    return currentLocale == Locale("fa").language || Constants.PERSIAN_REGEX.containsMatchIn(text)
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun getCurrentLocale(context: Context): Locale = context.resources.configuration.locales.get(0)

fun String.log() {
    if (BuildConfig.DEBUG)
        Log.d(Constants.LOG_TAG, this)
}