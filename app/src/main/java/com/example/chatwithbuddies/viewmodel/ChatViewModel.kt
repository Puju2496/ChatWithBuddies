package com.example.chatwithbuddies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.enqueue
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.models.Attachment
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.utils.ProgressCallback
import io.getstream.chat.android.livedata.ChatDomain
import timber.log.Timber
import java.io.File
import java.lang.reflect.Type
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val sharedPreferences: SharedPreferences) :
    ViewModel() {

    private val gson = Gson()
    private val type: Type = object : TypeToken<List<Message?>?>() {}.type

    fun deleteMessage(id: String) {
        ChatClient.instance().deleteMessage(messageId = id, false)
            .enqueue()
    }

    fun addIdToStarredMessage(message: Message?) {
        message?.let {
            val messages = sharedPreferences.getString(STARRED_MESSAGES, "")
            val messagesList =
                gson.fromJson<MutableList<Message?>?>(messages, type) ?: mutableListOf()
            messagesList.add(it)

            sharedPreferences.edit().remove(STARRED_MESSAGES).apply()
            sharedPreferences.edit().putString(STARRED_MESSAGES, gson.toJson(messagesList)).apply()
        }
    }

    fun removeStarredMessage(message: Message?) {
        val messages = sharedPreferences.getString(STARRED_MESSAGES, "")
        val messagesList = gson.fromJson<MutableList<Message?>?>(messages, type) ?: mutableListOf()
        messagesList.removeIf { it?.id == message?.id }

        sharedPreferences.edit().remove(STARRED_MESSAGES).apply()
        sharedPreferences.edit().putString(STARRED_MESSAGES, gson.toJson(messagesList)).apply()
    }

    fun isStarredMessage(message: Message?): Boolean {
        val messages = sharedPreferences.getString(STARRED_MESSAGES, "")
        val messagesList = gson.fromJson<MutableList<Message?>?>(messages, type) ?: mutableListOf()

        return messagesList.any { it?.id == message?.id }
    }

    fun sendMessage(channelType: String, channelId: String, message: Message) {
        ChatClient.instance()
            .sendMessage(channelType, channelId, message)
            .enqueue()
    }

    fun sendVoiceMessage(channelType: String, channelId: String, file: String) {
        val attachment = Attachment(
            type = "audio",
            upload = File(file)
        )

        val message = Message(attachments = mutableListOf(attachment))
        ChatClient.instance().sendMessage(channelType, channelId, message).enqueue()
    }

    companion object {
        const val STARRED_MESSAGES = "starred_messages"
    }
}