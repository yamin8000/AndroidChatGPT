/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     Utility.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     Utility.kt Last modified at 2023/12/1
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

package io.github.yamin8000.chatgpt.util

import android.content.Context
import android.util.Log
import java.util.Locale

fun Context.isLocalePersian(text: String): Boolean {
    val currentLocale = getCurrentLocale(this).language
    return currentLocale == Locale("fa").language || Constants.PERSIAN_REGEX.containsMatchIn(text)
}

fun getCurrentLocale(context: Context): Locale = context.resources.configuration.locales.get(0)

fun log(
    message: String
) {
    Log.d(Constants.LOG_TAG, message)
}

fun log(
    exception: Exception
) {
    log(exception.stackTraceToString())
}