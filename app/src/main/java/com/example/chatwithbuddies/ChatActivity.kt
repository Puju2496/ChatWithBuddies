package com.example.chatwithbuddies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.chatwithbuddies.databinding.ActivityChatBinding
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.ui.message.input.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel
import io.getstream.chat.android.ui.message.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.Mode.Thread
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.Mode.Normal
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.State.NavigateUp
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.Event.BackButtonPressed
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.channel.subscribeFor
import io.getstream.chat.android.client.events.TypingStartEvent
import io.getstream.chat.android.client.events.TypingStopEvent
import timber.log.Timber

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private var channelId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        channelId = checkNotNull(intent.getStringExtra(CHANNEL_ID)) {
            "Specifying a channel id is required when starting ChatActivity"
        }

        binding.messages.setMessageViewHolderFactory(ChatViewHolderFactory())

        val viewModelFactory = MessageListViewModelFactory(channelId)

        val headerViewModel: MessageListHeaderViewModel by viewModels { viewModelFactory }
        val listViewModel: MessageListViewModel by viewModels { viewModelFactory }
        val inputViewModel: MessageInputViewModel by viewModels { viewModelFactory }

        headerViewModel.bindView(binding.header, this)
        listViewModel.bindView(binding.messages, this)
        inputViewModel.bindView(binding.input, this)

        listViewModel.mode.observe(this) {
            when (it) {
                is Thread -> {
                    headerViewModel.setActiveThread(it.parentMessage)
                    inputViewModel.setActiveThread(it.parentMessage)
                }
                is Normal -> {
                    headerViewModel.resetThread()
                    inputViewModel.resetThread()
                }
            }
        }

        binding.messages.setMessageEditHandler(inputViewModel::postMessageToEdit)

        listViewModel.state.observe(this) {
            if (it is NavigateUp) {
                finish()
            }
        }

        val backHandler = {
            listViewModel.onEvent(BackButtonPressed)
        }

        binding.header.setBackButtonClickListener(backHandler)

        onBackPressedDispatcher.addCallback(this) {
            backHandler()
        }

        val typingList = mutableSetOf<String>()
        ChatClient
            .instance()
            .channel(channelId)
            .subscribeFor(
                this,
                TypingStartEvent::class,
                TypingStopEvent::class
            ) {
                when (it) {
                    is TypingStartEvent -> typingList.add(it.user.name)
                    is TypingStopEvent -> typingList.remove(it.user.name)
                    else -> { }
                }

                when {
                    typingList.isNotEmpty() -> {
                        binding.typing.text = typingList.joinToString(prefix = "Typing: ", separator = ",")
                        binding.typing.visibility = View.VISIBLE
                    }
                    else -> binding.typing.visibility = View.GONE
                }
            }
    }

    companion object {
        const val CHANNEL_ID = "channel_id"
    }
}