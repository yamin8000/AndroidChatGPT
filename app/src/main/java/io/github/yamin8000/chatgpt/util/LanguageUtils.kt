/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     LanguageUtils.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     LanguageUtils.kt Last modified at 2023/12/1
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

import androidx.core.os.LocaleListCompat
import java.util.*

object LanguageUtils {

    fun LocaleListCompat.toList(): List<Locale> {
        val list = mutableListOf<Locale>()
        for (i in 0 until this.size()) {
            val locale = this.get(i)
            if (locale != null)
                list.add(locale)
        }
        return list
    }

    fun Locale.setDefault(): LocaleListCompat {
        var newTags = Constants.DEFAULT_LANGUAGE_TAGS
        newTags = newTags.replace(this.toLanguageTag(), "")
            .replace(",,", ",")
            .removeSuffix(",")
            .removePrefix(",")
        newTags = "${this.toLanguageTag()},${newTags}"
        return LocaleListCompat.forLanguageTags(newTags)
    }
}