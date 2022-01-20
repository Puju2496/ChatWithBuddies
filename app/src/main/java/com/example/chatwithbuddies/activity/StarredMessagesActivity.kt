package com.example.chatwithbuddies.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatwithbuddies.R
import com.example.chatwithbuddies.adapter.StarredListAdapter
import com.example.chatwithbuddies.databinding.ActivityStarredMessagesBinding
import com.example.chatwithbuddies.model.StarredMessage
import com.example.chatwithbuddies.viewmodel.ChatViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.Message
import timber.log.Timber
import java.lang.reflect.Type
import javax.inject.Inject

@AndroidEntryPoint
class StarredMessagesActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityStarredMessagesBinding

    private val type: Type = object : TypeToken<List<Message?>?>() {}.type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStarredMessagesBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val messages = sharedPreferences.getString(ChatViewModel.STARRED_MESSAGES, "")
        if (messages.isNullOrEmpty()) {
            binding.noMessages.visibility = View.VISIBLE
            binding.messages.visibility = View.GONE
        } else {
            binding.messages.visibility = View.VISIBLE
            binding.noMessages.visibility = View.GONE
        }

        val messagesList = Gson().fromJson<MutableList<Message?>?>(messages, type) ?: mutableListOf()

        binding.messages.apply {
            layoutManager = LinearLayoutManager(this@StarredMessagesActivity)
            adapter = StarredListAdapter().apply {
                this.messages = messagesList.map {
                    StarredMessage().apply {
                        setMessage(it)
                    }
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}