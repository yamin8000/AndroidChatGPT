package io.github.rayangroup.hamyar.db

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class DateConverter {

    @TypeConverter
    fun epochToDatetime(epoch: Long): ZonedDateTime {
        val instant = getInstant(epoch)
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    @TypeConverter
    fun datetimeToEpoch(dateTime: ZonedDateTime): Long = dateTime.toEpochSecond()

    private fun getInstant(epoch: Long) = Instant.ofEpochSecond(epoch)

    @TypeConverter
    fun epochToDate(epoch: Long): LocalDate = LocalDate.ofEpochDay(epoch)

    @TypeConverter
    fun dateToEpoch(date: LocalDate): Long = date.toEpochDay()
}