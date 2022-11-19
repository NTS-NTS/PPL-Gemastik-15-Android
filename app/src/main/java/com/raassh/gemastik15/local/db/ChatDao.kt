package com.raassh.gemastik15.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats: List<ChatEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM chats")
    fun getChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId")
    fun getMessages(chatId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp DESC LIMIT 1")
    fun getLastMessage(chatId: String): Flow<MessageEntity>

    @Query("SELECT * FROM messages WHERE content LIKE '%' || :query || '%'")
    fun searchChat(query: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM chats WHERE id IN (:chatIds)")
    fun getChatByIds(chatIds: List<String>): List<ChatEntity>

    @Query("SELECT * FROM chats WHERE users = :sender")
    fun getChatByUserId(sender: String): ChatEntity?
}