package io.github.rayangroup.hamyar.util

import android.content.Context
import android.os.Build
import android.util.Log
import io.github.rayangroup.hamyar.BuildConfig
import io.github.rayangroup.hamyar.util.DateTimeUtils.toIso
import io.github.rayangroup.hamyar.web.Web
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

private const val GITHUB_URL = "https://api.github.com/repos/RayanGroup/HamyarExceptions/issues"

fun Context.isLocalePersian(text: String): Boolean {
    val currentLocale = getCurrentLocale(this).language
    return currentLocale == Locale("fa").language || Constants.PERSIAN_REGEX.containsMatchIn(text)
}

fun getCurrentLocale(context: Context): Locale = context.resources.configuration.locales.get(0)

fun log(
    message: String
) {
    if (BuildConfig.DEBUG)
        Log.d(Constants.LOG_TAG, message)
}

fun log(
    exception: Exception
) {
    log(exception.stackTraceToString())
}

fun reportException(
    exception: Exception
) {
    try {
        Web.getLogOkHttp().newCall(
            Request.Builder()
                .url(GITHUB_URL)
                .post(exceptionToRequestBody(exception))
                .build()
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                log("${exception.message} report failed! ${e.message}")
                log(e)
            }

            override fun onResponse(call: Call, response: Response) {
                log("${exception.message}, report success: ${response.message}: ${response.code}")
                log(response.body?.string() ?: "Log empty body")
            }
        })
    } catch (e: Exception) {
        log(e)
    }
}

private fun exceptionToRequestBody(
    exception: Exception
) = JSONObject().apply {
    put("title", exception.message)
    put("assignees", JSONArray(arrayOf("yamin8000")))
    put("labels", JSONArray(arrayOf("bug")))
    put("body", buildString {
        append("Time: ${(Build.TIME / 1000).toIso()}\n")
        append("App Build Type: ${BuildConfig.BUILD_TYPE}\n")
        append("App Version Code: ${BuildConfig.VERSION_CODE}\n")
        append("App Version Name: ${BuildConfig.VERSION_NAME}\n")
        append("Android Version: ${Build.VERSION.RELEASE}\n")
        append("Brand: ${Build.BRAND}\n")
        append("Model: ${Build.MODEL}\n")
        append("Device: ${Build.DEVICE}\n")
        append("Product: ${Build.PRODUCT}\n")
        append("Tags: ${Build.TAGS}\n")
        append("Stacktrace:\n")
        append("```\n")
        append(exception.stackTraceToString())
        append("```")
    })
}.toString().toRequestBody()