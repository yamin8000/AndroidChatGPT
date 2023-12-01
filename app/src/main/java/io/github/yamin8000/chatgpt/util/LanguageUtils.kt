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