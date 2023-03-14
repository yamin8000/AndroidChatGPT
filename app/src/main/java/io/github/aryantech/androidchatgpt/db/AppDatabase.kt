package io.github.aryantech.androidchatgpt.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.aryantech.androidchatgpt.db.dao.DAOs
import io.github.aryantech.androidchatgpt.db.entity.HistoryEntity
import io.github.aryantech.androidchatgpt.db.entity.HistoryItemEntity

@TypeConverters(DateConverter::class)
@Database(
    entities = [HistoryEntity::class, HistoryItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): DAOs.HistoryDao

    abstract fun historyItemDao(): DAOs.HistoryItemDao
}