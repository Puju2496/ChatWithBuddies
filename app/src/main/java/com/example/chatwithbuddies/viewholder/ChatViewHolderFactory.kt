package com.example.chatwithbuddies.viewholder

import android.content.SharedPreferences
import android.view.ViewGroup
import com.example.chatwithbuddies.viewmodel.ChatViewModel
import com.getstream.sdk.chat.adapter.MessageListItem
import io.getstream.chat.android.ui.common.extensions.isDeleted
import io.getstream.chat.android.ui.message.list.adapter.BaseMessageItemViewHolder
import io.getstream.chat.android.ui.message.list.adapter.MessageListItemViewHolderFactory
import timber.log.Timber

class ChatViewHolderFactory(
    private val viewModel: ChatViewModel,
    private val sharedPreferences: SharedPreferences
) : MessageListItemViewHolderFactory() {

    override fun createViewHolder(
        parentView: ViewGroup,
        viewType: Int
    ): BaseMessageItemViewHolder<out MessageListItem> {
        return when (viewType) {
            TEXT_VIEW_HOLDER_TYPE -> ChatViewHolder(parentView, viewModel, sharedPreferences)
            DELETE_VIEW_HOLDER_TYPE -> DeletedMessageViewHolder(parentView)
            AUDIO_VIEW_HOLDER_TYPE -> AudioViewHolder(parentView, viewModel)
            else -> super.createViewHolder(parentView, viewType)
        }
    }

    override fun getItemViewType(item: MessageListItem): Int {
        val isAudioFile = !(item as? MessageListItem.MessageItem)?.message?.attachments?.filter { it.type == AUDIO_TYPE }.isNullOrEmpty()
        return if (item is MessageListItem.MessageItem && item.message.isDeleted()) {
            DELETE_VIEW_HOLDER_TYPE
        } else if (item is MessageListItem.MessageItem && isAudioFile) {
            AUDIO_VIEW_HOLDER_TYPE
        } else if (item is MessageListItem.MessageItem && item.message.attachments.isEmpty())
            TEXT_VIEW_HOLDER_TYPE
        else
            super.getItemViewType(item)
    }

    companion object {
        const val TEXT_VIEW_HOLDER_TYPE = 1
        const val DELETE_VIEW_HOLDER_TYPE = 2
        const val AUDIO_VIEW_HOLDER_TYPE = 3

        private const val AUDIO_TYPE = "audio"
    }
}