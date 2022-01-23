package com.example.chatwithbuddies.chatclient

import android.content.Context
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.livedata.ChatDomain


class ChatClient {

    fun initializeChat(context: Context) {
        if (INSTANCE == null) {
            INSTANCE = ChatClient.Builder("aztp5j9w29h4", context)
                .logLevel(ChatLogLevel.ALL)
                .build()

            ChatDomain.Builder(INSTANCE!!, context).build()
        }
    }

    companion object {
        var INSTANCE: ChatClient? = null
    }
}