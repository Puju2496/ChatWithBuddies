package com.example.chatwithbuddies

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.chatwithbuddies.databinding.LayoutMessageItemBinding
import com.getstream.sdk.chat.adapter.MessageListItem
import io.getstream.chat.android.ui.message.list.adapter.BaseMessageItemViewHolder
import io.getstream.chat.android.ui.message.list.adapter.MessageListItemPayloadDiff
import timber.log.Timber

class ChatViewHolder(
    parent: ViewGroup,
    private val binding: LayoutMessageItemBinding = LayoutMessageItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    )
): BaseMessageItemViewHolder<MessageListItem.MessageItem>(binding.root) {

    override fun bindData(data: MessageListItem.MessageItem, diff: MessageListItemPayloadDiff?) {
        Timber.d("<<>> called")
        binding.message.text = data.message.text
        binding.timeStamp.text = data.message.createdAt.toString()
    }
}