package com.example.chatwithbuddies.viewholder

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.example.chatwithbuddies.AdditionalDialog
import com.example.chatwithbuddies.R
import com.example.chatwithbuddies.databinding.LayoutMessageItemBinding
import com.example.chatwithbuddies.viewmodel.ChatViewModel
import com.getstream.sdk.chat.adapter.MessageListItem
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.ui.message.list.adapter.BaseMessageItemViewHolder
import io.getstream.chat.android.ui.message.list.adapter.MessageListItemPayloadDiff
import java.text.SimpleDateFormat
import java.util.*

class ChatViewHolder(
    parent: ViewGroup,
    private val viewModel: ChatViewModel,
    private val sharedPreferences: SharedPreferences,
    private val binding: LayoutMessageItemBinding = LayoutMessageItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    )
): BaseMessageItemViewHolder<MessageListItem.MessageItem>(binding.root), SharedPreferences.OnSharedPreferenceChangeListener {

    private val format = SimpleDateFormat("HH:mm", Locale.getDefault())

    private var message: Message? = null

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun bindData(data: MessageListItem.MessageItem, diff: MessageListItemPayloadDiff?) {
        message = data.message

        binding.card.updateLayoutParams { this as ConstraintLayout.LayoutParams
            startToStart = if (data.isTheirs) ConstraintLayout.LayoutParams.PARENT_ID else ConstraintLayout.LayoutParams.UNSET
            endToEnd = if (data.isTheirs) ConstraintLayout.LayoutParams.UNSET else ConstraintLayout.LayoutParams.PARENT_ID
        }

        if (data.isMine) {
            binding.card.setCardBackgroundColor(context.getColorStateList(R.color.mineChatBackground))
            binding.message.setTextColor(context.getColor(R.color.mineChatText))
            binding.timeStamp.setTextColor(context.getColor(R.color.mineChatText))
        } else if ((data.isTheirs)) {
            binding.card.setCardBackgroundColor(context.getColor(R.color.othersChatBackground))
            binding.message.setTextColor(context.getColor(R.color.titleColor))
            binding.timeStamp.setTextColor(context.getColor(R.color.titleColor))
            binding.readStatus.visibility = View.GONE
        }

        updateStarredIcon(message)

        binding.message.text = data.message.text
        binding.timeStamp.text = format.format(data.message.createdAt ?: Date())

        binding.root.setOnLongClickListener {
            AdditionalDialog(context, viewModel).showBottomSheet(data.message)
            return@setOnLongClickListener true
        }
    }

    private fun updateStarredIcon(message: Message?) {
        binding.starred.isVisible = viewModel.isStarredMessage(message)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, p1: String?) {
        if (p1 == ChatViewModel.STARRED_MESSAGES)
            updateStarredIcon(message)
    }
}