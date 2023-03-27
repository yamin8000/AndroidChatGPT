package io.github.RayanGroup.hamyar.db.dao

import androidx.room.Dao
import io.github.RayanGroup.hamyar.db.entity.HistoryEntity
import io.github.RayanGroup.hamyar.db.entity.HistoryItemEntity

object DAOs {

    @Dao
    abstract class HistoryDao : BaseDao<HistoryEntity>("HistoryEntity")

    @Dao
    abstract class HistoryItemDao : BaseDao<HistoryItemEntity>("HistoryItemEntity")
}