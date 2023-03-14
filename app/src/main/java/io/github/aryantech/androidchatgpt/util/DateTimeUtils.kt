package io.github.aryantech.androidchatgpt.util

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