package com.example.chatwithbuddies.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.chatwithbuddies.AdditionalDialog
import com.example.chatwithbuddies.viewholder.ChatViewHolderFactory
import com.example.chatwithbuddies.databinding.ActivityChatBinding
import com.example.chatwithbuddies.viewmodel.ChatViewModel
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.Event.BackButtonPressed
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.Mode.Normal
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.Mode.Thread
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.State.NavigateUp
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.channel.subscribeFor
import io.getstream.chat.android.client.events.TypingStartEvent
import io.getstream.chat.android.client.events.TypingStopEvent
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel
import io.getstream.chat.android.ui.message.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private val chatViewModel by viewModels<ChatViewModel>()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var channelCid: String = ""
    private var channelId: String = ""
    private var channelType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        channelCid = checkNotNull(intent.getStringExtra(CHANNEL_CID)) {
            "Specifying a channel id is required when starting ChatActivity"
        }
        channelId = intent.getStringExtra(CHANNEL_ID).orEmpty()
        channelType = intent.getStringExtra(CHANNEL_TYPE).orEmpty()

        binding.messages.setMessageViewHolderFactory(ChatViewHolderFactory(chatViewModel, sharedPreferences))

        val viewModelFactory = MessageListViewModelFactory(channelCid)

        val headerViewModel: MessageListHeaderViewModel by viewModels { viewModelFactory }
        val listViewModel: MessageListViewModel by viewModels { viewModelFactory }

        headerViewModel.bindView(binding.header, this)
        listViewModel.bindView(binding.messages, this)
        binding.input.apply {
            send.setOnClickListener {
                if (inputEdittext.text?.isNotEmpty() == true) {
                    chatViewModel.sendMessage(channelType, channelId, Message(cid = channelCid, text = inputEdittext.text?.toString().orEmpty()))
                    inputEdittext.text?.clear()
                }
            }

            voice.setOnClickListener {

            }
        }

        listViewModel.mode.observe(this) {
            when (it) {
                is Thread -> {
                    headerViewModel.setActiveThread(it.parentMessage)
                    binding.input.inputEdittext.text?.clear()
                }
                is Normal -> {
                    headerViewModel.resetThread()
                    binding.input.inputEdittext.text?.clear()
                }
            }
        }

        binding.messages.setMessageLongClickListener {
            AdditionalDialog(this, chatViewModel).showBottomSheet(it)
        }

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
            .channel(channelCid)
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
        const val CHANNEL_TYPE = "channel_type"
        const val CHANNEL_CID = "channel_cid"

        fun instance(context: Context, channel: Channel) = Intent(context, ChatActivity::class.java).apply {
            putExtra(CHANNEL_CID, channel.cid)
            putExtra(CHANNEL_ID, channel.id)
            putExtra(CHANNEL_TYPE, channel.type)
        }
    }
}