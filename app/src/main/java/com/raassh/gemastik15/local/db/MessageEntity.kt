package com.raassh.gemastik15.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val chatId: String,
    val sender: String,
    val content: String,
    val timestamp: Long
)