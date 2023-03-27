package io.github.rayan_group.hamyar.db.dao

import androidx.room.Dao
import io.github.rayan_group.hamyar.db.entity.HistoryEntity
import io.github.rayan_group.hamyar.db.entity.HistoryItemEntity

object DAOs {

    @Dao
    abstract class HistoryDao : BaseDao<HistoryEntity>("HistoryEntity")

    @Dao
    abstract class HistoryItemDao : BaseDao<HistoryItemEntity>("HistoryItemEntity")
}