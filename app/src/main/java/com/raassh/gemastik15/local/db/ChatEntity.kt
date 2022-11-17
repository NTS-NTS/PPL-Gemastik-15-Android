package com.raassh.gemastik15.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity (
    @PrimaryKey
    val id: String,
    val users: String
)