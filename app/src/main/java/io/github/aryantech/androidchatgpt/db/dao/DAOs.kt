package io.github.aryantech.androidchatgpt.db.dao

import androidx.room.Dao
import io.github.aryantech.androidchatgpt.db.entity.HistoryEntity
import io.github.aryantech.androidchatgpt.db.entity.HistoryItemEntity

object DAOs {

    @Dao
    abstract class HistoryDao : BaseDao<HistoryEntity>("HistoryEntity")

    @Dao
    abstract class HistoryItemDao : BaseDao<HistoryItemEntity>("HistoryItemEntity")
}