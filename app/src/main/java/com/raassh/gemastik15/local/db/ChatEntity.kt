package com.raassh.gemastik15.local.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "chats")
@Parcelize
data class ChatEntity (
    @PrimaryKey
    val id: String,
    val users: String
) : Parcelable