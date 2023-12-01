package io.github.yamin8000.chatgpt.db.dao

import androidx.room.Dao
import io.github.yamin8000.chatgpt.db.entity.HistoryEntity
import io.github.yamin8000.chatgpt.db.entity.HistoryItemEntity

object DAOs {

    @Dao
    abstract class HistoryDao : BaseDao<HistoryEntity>("HistoryEntity")

    @Dao
    abstract class HistoryItemDao : BaseDao<HistoryItemEntity>("HistoryItemEntity")
}