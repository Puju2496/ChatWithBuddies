package com.example.chatwithbuddies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatwithbuddies.databinding.LayoutStarredMessageItemBinding
import com.example.chatwithbuddies.model.StarredMessage

class StarredListAdapter: RecyclerView.Adapter<StarredListAdapter.MessageViewHolder>() {

    var messages: List<StarredMessage>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutStarredMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
       holder.bind(messages?.get(position))
    }

    override fun getItemCount(): Int = messages?.size ?: 0

    class MessageViewHolder(private val binding: LayoutStarredMessageItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(message: StarredMessage?) {
            binding.viewModel = message
            binding.executePendingBindings()
        }
    }
}