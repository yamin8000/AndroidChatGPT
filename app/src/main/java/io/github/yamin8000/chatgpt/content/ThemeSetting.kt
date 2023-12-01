package io.github.yamin8000.chatgpt.content

import android.os.Parcelable
import androidx.annotation.StringRes
import io.github.yamin8000.chatgpt.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ThemeSetting(
    @StringRes val persianNameStringResource: Int
) : Parcelable {
    Dark(R.string.theme_dark), Light(R.string.theme_light), System(R.string.theme_system);
}