/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     DateTimeUtils.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     DateTimeUtils.kt Last modified at 2023/12/1
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

import java.time.*
import java.time.format.DateTimeFormatter

@Suppress("unused", "MemberVisibilityCanBePrivate")
object DateTimeUtils {

    fun now(): String = LocalDateTime.now().toIso()

    fun zonedNow(): ZonedDateTime = ZonedDateTime.now()

    fun String.toDateTime(): LocalDateTime = LocalDateTime.parse(this)

    fun LocalDateTime.toIso(): String = DateTimeFormatter.ISO_DATE_TIME.format(this)

    fun Long.toIso() = this.toDateTime().toIso()

    fun Long.toDateTime(): LocalDateTime {
        return LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)
    }

    fun Long.toZonedDateTime(): ZonedDateTime {
        return ZonedDateTime.of(this.toDateTime(), ZoneId.systemDefault())
    }

    fun Long.zonedDateTimeOfMillis(): ZonedDateTime {
        return this.toLocalDateTime().toZonedDateTime()
    }

    fun LocalDateTime.toZonedDateTime(): ZonedDateTime {
        return ZonedDateTime.of(this, ZoneId.systemDefault())
    }

    fun Long.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    }

    fun LocalDateTime.ignoreTime(): LocalDateTime {
        return this.withHour(0).withMinute(0).withSecond(0).withNano(0)
    }
}