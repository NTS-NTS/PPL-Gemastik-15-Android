package com.raassh.gemastik15.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.raassh.gemastik15.BuildConfig
import com.raassh.gemastik15.R
import com.raassh.gemastik15.local.db.ChatEntity
import com.raassh.gemastik15.local.db.MessageEntity
import com.raassh.gemastik15.repository.ChatRepository
import com.raassh.gemastik15.view.activity.dashboard.DashboardActivity
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatService : Service(), KoinComponent {

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not supported")
    }

    private var mSocket: Socket? = null
    private var userId = ""
    private val chatRepository by inject<ChatRepository>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val extras = intent?.extras
        userId = extras?.getString(USER_ID) ?: ""

        Log.d("ChatService", "onStartCommand: $userId")

        val options = IO.Options.builder()
            .setExtraHeaders(mapOf(
                "x-user-id" to listOf(userId),
            ))
            .build()

        mSocket = IO.socket(BuildConfig.BASE_URL, options)

        mSocket?.apply {
            connect()
            on("connected", OnConnected)
            on("chat_list_response", onChatListResponse)
            on("new_chat", onNewChat)
            on("new_message", onNewMessage)
        }

        return START_REDELIVER_INTENT
    }

    private val OnConnected = Emitter.Listener {
        it.forEach { test ->
            Log.d("ChatService", "connected: $test")
        }
        mSocket?.emit("get_chat_list", userId)
    }

    private val onChatListResponse = Emitter.Listener {
        try {
            val chatList = it[0] as JSONArray

            val chats = mutableListOf<ChatEntity>()
            val messages = mutableListOf<MessageEntity>()

            for (i in 0 until chatList.length()) {
                val chat = chatList.getJSONObject(i)
                val chatId = chat.getString("chat_id")
                val users = chat.getJSONArray("users")
                val messagesInChat = chat.getJSONArray("messages")

                val stringBuilder = StringBuilder()
                for (j in 0 until users.length()) {
                    stringBuilder.append(users.getString(j))
                    if (j < users.length() - 1) {
                        stringBuilder.append(" ")
                    }
                }

                chats.add(ChatEntity(
                    id = chatId,
                    users = stringBuilder.toString()
                ))

                for (j in 0 until messagesInChat.length()) {
                    val message = messagesInChat.getJSONObject(j)
                    val sender = message.getString("sender")
                    val content = message.getString("content")
                    val timestamp = message.getLong("timestamp")

                    messages.add(MessageEntity(
                        id = "$chatId-$sender-$timestamp",
                        chatId = chatId,
                        sender = sender,
                        content = content,
                        timestamp = timestamp
                    ))
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                chatRepository.insertChats(chats)
                chatRepository.insertMessages(messages)
            }
        } catch (e: Exception) {
            Log.e("ChatService", "onChatListResponse: ${e.message}")
        }
    }

    private val onNewChat = Emitter.Listener {
        try {
            val chat = it[0] as JSONObject
            val chatId = chat.getString("chat_id")
            val users = chat.getJSONArray("users")
            val messagesInChat = chat.getJSONArray("messages")

            val stringBuilder = StringBuilder()
            for (j in 0 until users.length()) {
                stringBuilder.append(users.getString(j))
                if (j < users.length() - 1) {
                    stringBuilder.append(" ")
                }
            }

            val messages = mutableListOf<MessageEntity>()

            for (j in 0 until messagesInChat.length()) {
                val message = messagesInChat.getJSONObject(j)
                val id = message.getString("id")
                val sender = message.getString("sender")
                val content = message.getString("content")
                val timestamp = message.getLong("timestamp")

                messages.add(MessageEntity(
                    id = id,
                    chatId = chatId,
                    sender = sender,
                    content = content,
                    timestamp = timestamp
                ))

                if (sender != userId) {
                    showNotification(content, sender, chatId)
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                chatRepository.insertChat(ChatEntity(
                    id = chatId,
                    users = stringBuilder.toString()
                ))

                chatRepository.insertMessages(messages)
            }
        } catch (e: Exception) {
            Log.e("ChatService", "onNewChat: ${e.message}")
        }
    }

    private val onNewMessage = Emitter.Listener {
        try {
            val message = it[0] as JSONObject
            val id = message.getString("id")
            val chatId = message.getString("chat_id")
            val sender = message.getString("sender")
            val content = message.getString("content")
            val timestamp = message.getLong("timestamp")

//            TODO: generate message id
            CoroutineScope(Dispatchers.IO).launch {
                chatRepository.insertMessage(MessageEntity(
                    id = id,
                    chatId = chatId,
                    sender = sender,
                    content = content,
                    timestamp = timestamp
                ))

                if (sender != userId) {
                    showNotification(content, sender, chatId)
                }
            }
        } catch (e: Exception) {
            Log.e("ChatService", "onNewMessage: ${e.message}")
        }
    }

    private fun showNotification(message: String, sender: String, chatId: String) {
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(DashboardActivity::class.java)
            .setGraph(R.navigation.dashboard_nav)
            .setDestination(R.id.chatFragment)
            .setArguments(bundleOf(
                "chat" to ChatEntity(
                    id = chatId,
                    users = sender
                )
            ))
            .createPendingIntent()

        val notifManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifBuilder = NotificationCompat
            .Builder(applicationContext, "Chat")
            .setSmallIcon(R.drawable.ic_baseline_chat_24)
            .setContentTitle(sender)
            .setContentText(message)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("Chat", "Chat", NotificationManager.IMPORTANCE_DEFAULT)
                    .apply {
                        description = "Chat"
                    }

            notifBuilder.setChannelId("Chat")
            notifManager.createNotificationChannel(channel)
        }

        val notif = notifBuilder.build()
        notifManager.notify(1, notif)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.off()
        mSocket?.disconnect()

        Log.d("ChatService", "socket stopped")
    }

    companion object {
        const val USER_ID = "user_id"
    }
}