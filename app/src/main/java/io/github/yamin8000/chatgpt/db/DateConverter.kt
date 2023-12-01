/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     DateConverter.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     DateConverter.kt Last modified at 2023/12/1
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

package io.github.yamin8000.chatgpt.db

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