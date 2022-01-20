package com.example.chatwithbuddies

import android.view.ViewGroup
import com.example.chatwithbuddies.viewmodel.ChatViewModel
import com.getstream.sdk.chat.adapter.MessageListItem
import io.getstream.chat.android.ui.message.list.adapter.BaseMessageItemViewHolder
import io.getstream.chat.android.ui.message.list.adapter.MessageListItemViewHolderFactory

class ChatViewHolderFactory(private val viewModel: ChatViewModel): MessageListItemViewHolderFactory() {

    override fun createViewHolder(
        parentView: ViewGroup,
        viewType: Int
    ): BaseMessageItemViewHolder<out MessageListItem> {
        return when (viewType) {
            TEXT_VIEW_HOLDER_TYPE -> ChatViewHolder(parentView, viewModel)
            else -> super.createViewHolder(parentView, viewType)
        }
    }

    override fun getItemViewType(item: MessageListItem): Int {
        return if (item is MessageListItem.MessageItem && item.message.attachments.isEmpty())
            TEXT_VIEW_HOLDER_TYPE
        else
            super.getItemViewType(item)
    }

    companion object {
        const val TEXT_VIEW_HOLDER_TYPE = 1
    }
}