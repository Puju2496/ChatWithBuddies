package com.example.chatwithbuddies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.chatwithbuddies.activity.HomeActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.FilterObject
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.call.enqueue
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.GuestUser
import io.getstream.chat.android.client.models.User
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val sharedPreferences: SharedPreferences): ViewModel() {
    private var client: ChatClient? = null
    fun initializeChatDomain(): FilterObject {
        client = ChatClient.instance()

        val userId = sharedPreferences.getString(HomeActivity.USER_ID, "").orEmpty()
        val userName = sharedPreferences.getString(HomeActivity.USER_NAME, "").orEmpty()

        val user = User(id = userId).apply {
            name = userName
            image = "https://bit.ly/2TIt8NR"
        }

        val token = client?.devToken(userId) ?: ""
        val guest = GuestUser(user, token)

        client?.connectUser(user, token)
            ?.enqueue()

        return Filters.or(
            Filters.eq("type", "messaging"),
            Filters.`in`("members", listOf(user.id))
        )
    }

    fun addChannel() {
        val channel = client?.channel("messaging", "general")
        channel?.create()?.enqueue()
    }

    fun logOutUser() {
        client?.disconnect()
    }
}