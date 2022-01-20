package com.example.chatwithbuddies.viewmodel

import androidx.lifecycle.ViewModel
import io.getstream.chat.android.client.ChatClient

class ChatViewModel: ViewModel() {
    fun deleteMessage(id: String) {
        ChatClient.instance().deleteMessage(messageId = id, false)
            .enqueue()
    }
}