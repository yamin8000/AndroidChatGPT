package io.github.RayanGroup.hamyar.content

import android.os.Parcelable
import androidx.annotation.StringRes
import io.github.RayanGroup.hamyar.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ThemeSetting(
    @StringRes val persianNameStringResource: Int
) : Parcelable {
    Dark(R.string.theme_dark), Light(R.string.theme_light), System(R.string.theme_system);
}