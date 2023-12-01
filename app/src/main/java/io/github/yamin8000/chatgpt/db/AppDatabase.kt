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