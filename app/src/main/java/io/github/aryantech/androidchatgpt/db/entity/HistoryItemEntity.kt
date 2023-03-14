package io.github.aryantech.androidchatgpt.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.aryantech.androidchatgpt.content.chat.ChatBubbleOwner
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = HistoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["historyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HistoryItemEntity(
    val content: String,
    val owner: ChatBubbleOwner,
    @ColumnInfo(name = "historyId", index = true)
    val historyId: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
) : Parcelable
