package com.example.chatwithbuddies

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatwithbuddies.databinding.LayoutMessageInputBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    
    val binding = LayoutMessageInputBinding.inflate(LayoutInflater.from(context))

    init {
        LayoutMessageInputBinding.inflate(LayoutInflater.from(context))
    }
}