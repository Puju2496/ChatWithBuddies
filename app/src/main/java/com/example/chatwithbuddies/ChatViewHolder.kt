package com.example.chatwithbuddies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.example.chatwithbuddies.databinding.LayoutMessageItemBinding
import com.getstream.sdk.chat.adapter.MessageListItem
import io.getstream.chat.android.ui.message.list.adapter.BaseMessageItemViewHolder
import io.getstream.chat.android.ui.message.list.adapter.MessageListItemPayloadDiff
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ChatViewHolder(
    parent: ViewGroup,
    private val binding: LayoutMessageItemBinding = LayoutMessageItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    )
): BaseMessageItemViewHolder<MessageListItem.MessageItem>(binding.root) {

    private val format = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun bindData(data: MessageListItem.MessageItem, diff: MessageListItemPayloadDiff?) {
        if (data.isMine) {
            binding.card.setBackgroundColor(context.getColor(R.color.mineChatBackground))
            binding.message.setTextColor(context.getColor(R.color.mineChatText))
            binding.timeStamp.setTextColor(context.getColor(R.color.mineChatText))
        } else if ((data.isTheirs)) {
            binding.card.setBackgroundColor(context.getColor(R.color.othersChatBackground))
            binding.message.setTextColor(context.getColor(R.color.titleColor))
            binding.timeStamp.setTextColor(context.getColor(R.color.titleColor))
        }
        Timber.d("<<>> ${data.message.createdAt}")
        binding.message.text = data.message.text
        binding.timeStamp.text = format.format(data.message.createdAt ?: Date())
    }
}