package com.example.chatwithbuddies.viewholder

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.example.chatwithbuddies.R
import com.example.chatwithbuddies.databinding.LayoutVoiceMessageBinding
import com.example.chatwithbuddies.dialogs.AdditionalDialog
import com.example.chatwithbuddies.viewmodel.ChatViewModel
import com.getstream.sdk.chat.adapter.MessageListItem
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.ui.message.list.adapter.BaseMessageItemViewHolder
import io.getstream.chat.android.ui.message.list.adapter.MessageListItemPayloadDiff
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AudioViewHolder(
    parent: ViewGroup,
    private val viewModel: ChatViewModel,
    private val binding: LayoutVoiceMessageBinding = LayoutVoiceMessageBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    )
): BaseMessageItemViewHolder<MessageListItem.MessageItem>(binding.root) {

    private val format = SimpleDateFormat("HH:mm", Locale.getDefault())

    private var message: Message? = null

    override fun bindData(data: MessageListItem.MessageItem, diff: MessageListItemPayloadDiff?) {
        message = data.message

        binding.card.updateLayoutParams { this as ConstraintLayout.LayoutParams
            startToStart = if (data.isTheirs) ConstraintLayout.LayoutParams.PARENT_ID else ConstraintLayout.LayoutParams.UNSET
            endToEnd = if (data.isTheirs) ConstraintLayout.LayoutParams.UNSET else ConstraintLayout.LayoutParams.PARENT_ID
        }

        if (data.isMine) {
            binding.card.setCardBackgroundColor(context.getColorStateList(R.color.mineChatBackground))
            binding.timeStamp.setTextColor(context.getColor(R.color.mineChatText))
            binding.play.setColorFilter(ContextCompat.getColor(context, R.color.white))
        } else if ((data.isTheirs)) {
            binding.card.setCardBackgroundColor(context.getColor(R.color.othersChatBackground))
            binding.timeStamp.setTextColor(context.getColor(R.color.titleColor))
            binding.play.setColorFilter(ContextCompat.getColor(context, R.color.grey))
            binding.readStatus.visibility = View.GONE
        }

        binding.timeStamp.text = format.format(data.message.createdAt ?: Date())

        val attachment = message?.attachments?.get(0)
        /*val uri = Uri.parse(attachment?.upload?.absolutePath)
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, uri)
        val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        Timber.d("<<>> duration $duration")*/

        binding.root.setOnLongClickListener {
            AdditionalDialog(context, viewModel).showBottomSheet(data.message)
            return@setOnLongClickListener true
        }
    }
}