package io.github.aryantech.androidchatgpt.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
@Entity
data class HistoryEntity(
    val title: String,
    val date: ZonedDateTime,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
) : Parcelable