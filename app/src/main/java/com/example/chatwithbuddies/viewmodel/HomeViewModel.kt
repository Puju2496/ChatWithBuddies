package com.example.chatwithbuddies.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.FilterObject
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.livedata.ChatDomain

class HomeViewModel: ViewModel() {

    fun initializeChatDomain(context: Context): FilterObject {
        val client = ChatClient.Builder("b67pax5b2wdq", context)
            .logLevel(ChatLogLevel.ALL)
            .build()

        ChatDomain.Builder(client, context).build()

        val user = User(id = "tutorial-droid").apply {
            name = "Tomato"
            image = "https://bit.ly/2TIt8NR"
        }

        client.connectUser(
            user = user,
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtZHJvaWQifQ.NhEr0hP9W9nwqV7ZkdShxvi02C5PR7SJE7Cs4y7kyqg"
        )
            .enqueue()

        return Filters.and(
            Filters.eq("type", "messaging"),
            Filters.`in`("members", listOf(user.id))
        )
    }
}