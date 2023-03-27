package io.github.RayanGroup.hamyar.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.RayanGroup.hamyar.db.dao.DAOs
import io.github.RayanGroup.hamyar.db.entity.HistoryEntity
import io.github.RayanGroup.hamyar.db.entity.HistoryItemEntity

@TypeConverters(DateConverter::class)
@Database(
    entities = [HistoryEntity::class, HistoryItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): DAOs.HistoryDao

    abstract fun historyItemDao(): DAOs.HistoryItemDao
}