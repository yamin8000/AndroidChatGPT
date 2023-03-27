package io.github.rayan_group.hamyar.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.rayan_group.hamyar.db.dao.DAOs
import io.github.rayan_group.hamyar.db.entity.HistoryEntity
import io.github.rayan_group.hamyar.db.entity.HistoryItemEntity

@TypeConverters(DateConverter::class)
@Database(
    entities = [HistoryEntity::class, HistoryItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): DAOs.HistoryDao

    abstract fun historyItemDao(): DAOs.HistoryItemDao
}