package com.example.chatwithbuddies.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatwithbuddies.R
import com.example.chatwithbuddies.databinding.LayoutCustomDialogBinding
import com.example.chatwithbuddies.viewmodel.ChatViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.getstream.chat.android.client.models.Message

class AdditionalDialog(private val context: Context, private val viewModel: ChatViewModel): View.OnClickListener {

    private var binding: LayoutCustomDialogBinding = LayoutCustomDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = BottomSheetDialog(context)

    private var message: Message? = null
    private var isStarredMessage: Boolean = false

    init {
        dialog.setContentView(binding.root, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.resources.getDimensionPixelOffset(
            R.dimen.dialog_height
        )))
        binding.close.setOnClickListener(this)
        binding.deleteMessage.setOnClickListener(this)
        binding.starredMessage.setOnClickListener(this)
    }

    fun showBottomSheet(message: Message) {
        this.message = message
        isStarredMessage = viewModel.isStarredMessage(message)

        binding.starredMessage.text = if (isStarredMessage) context.getString(R.string.unstar_message) else context.getString(
            R.string.star_message
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.close -> dialog.dismiss()
            R.id.deleteMessage -> {
                dialog.dismiss()
                viewModel.deleteMessage(message?.id ?: "")
            }
            R.id.starredMessage -> {
                dialog.dismiss()
                if (isStarredMessage)
                    viewModel.removeStarredMessage(message)
                else
                    viewModel.addIdToStarredMessage(message)
            }
        }
    }
}