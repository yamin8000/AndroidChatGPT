/*
 *     AndroidChatGPT/AndroidChatGPT.app.main
 *     AppDatabase.kt Copyrighted by Yamin Siahmargooei at 2023/12/1
 *     AppDatabase.kt Last modified at 2023/12/1
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

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.yamin8000.chatgpt.db.dao.DAOs
import io.github.yamin8000.chatgpt.db.entity.HistoryEntity
import io.github.yamin8000.chatgpt.db.entity.HistoryItemEntity

@TypeConverters(DateConverter::class)
@Database(
    entities = [HistoryEntity::class, HistoryItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): DAOs.HistoryDao

    abstract fun historyItemDao(): DAOs.HistoryItemDao
}