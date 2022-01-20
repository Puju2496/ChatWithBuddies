package com.example.chatwithbuddies.model

import androidx.lifecycle.ViewModel
import io.getstream.chat.android.client.models.Message
import java.text.SimpleDateFormat
import java.util.*

class StarredMessage: ViewModel() {

    private val format = SimpleDateFormat("HH:mm", Locale.getDefault())

    private var message: Message? = null

    fun setMessage(message: Message?) {
        this.message = message
    }

    fun getMessage() = message

    fun getTimeStamp() = format.format(message?.createdAt ?: Date())
}