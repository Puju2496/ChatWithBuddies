package com.example.chatwithbuddies

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatwithbuddies.databinding.LayoutCustomDialogBinding
import com.example.chatwithbuddies.viewmodel.ChatViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class AdditionalDialog(private val context: Context, private val viewModel: ChatViewModel): View.OnClickListener {

    private var binding: LayoutCustomDialogBinding = LayoutCustomDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = BottomSheetDialog(context)

    private var messageId: String = ""

    init {
        dialog.setContentView(binding.root, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.resources.getDimensionPixelOffset(R.dimen.dialog_height)))
        binding.close.setOnClickListener(this)
        binding.deleteMessage.setOnClickListener(this)
    }

    fun showBottomSheet(id: String) {
        messageId = id
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.close -> dialog.dismiss()
            R.id.deleteMessage -> {
                dialog.dismiss()
                viewModel.deleteMessage(messageId)
            }
        }
    }
}