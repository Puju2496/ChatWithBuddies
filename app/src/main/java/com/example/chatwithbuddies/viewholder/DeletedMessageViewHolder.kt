package com.example.chatwithbuddies.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.example.chatwithbuddies.databinding.LayoutDeletedMessageBinding
import com.getstream.sdk.chat.adapter.MessageListItem
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.ui.message.list.adapter.BaseMessageItemViewHolder
import io.getstream.chat.android.ui.message.list.adapter.MessageListItemPayloadDiff

class DeletedMessageViewHolder(
    parent: ViewGroup,
    private val binding: LayoutDeletedMessageBinding = LayoutDeletedMessageBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    )
): BaseMessageItemViewHolder<MessageListItem.MessageItem>(binding.root) {


    private var message: Message? = null

    override fun bindData(data: MessageListItem.MessageItem, diff: MessageListItemPayloadDiff?) {
        message = data.message

        binding.delete.updateLayoutParams { this as ConstraintLayout.LayoutParams
            startToStart = if (data.isTheirs) ConstraintLayout.LayoutParams.PARENT_ID else ConstraintLayout.LayoutParams.UNSET
            endToEnd = if (data.isTheirs) ConstraintLayout.LayoutParams.UNSET else ConstraintLayout.LayoutParams.PARENT_ID
        }
    }
}