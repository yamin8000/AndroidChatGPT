package io.github.aryantech.androidchatgpt.util

import android.content.Context
import java.util.*

@Suppress("DEPRECATION")
fun getCurrentLocale(context: Context): Locale = context.resources.configuration.locales.get(0)