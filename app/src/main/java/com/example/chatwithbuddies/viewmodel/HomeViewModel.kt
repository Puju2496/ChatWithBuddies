package com.example.chatwithbuddies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.chatwithbuddies.activity.HomeActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.FilterObject
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val sharedPreferences: SharedPreferences): ViewModel() {
    private var client: ChatClient? = null
    fun initializeChatDomain(): FilterObject {
        client = ChatClient.instance()

        val userId = sharedPreferences.getString(HomeActivity.USER_ID, "").orEmpty()
        val userName = sharedPreferences.getString(HomeActivity.USER_NAME, "").orEmpty()

        val user = User(id = "tutorial-droid").apply {
            name = userName
            image = "https://bit.ly/2TIt8NR"
        }

        client?.connectGuestUser(userId, userName)
            ?.enqueue()

        return Filters.and(
            Filters.eq("type", "messaging"),
            Filters.`in`("members", listOf(user.id))
        )
    }
}