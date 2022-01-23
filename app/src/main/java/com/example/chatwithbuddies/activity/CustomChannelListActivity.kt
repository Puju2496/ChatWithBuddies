package com.example.chatwithbuddies.activity

import io.getstream.chat.android.ui.channel.ChannelListActivity
import io.getstream.chat.android.ui.channel.ChannelListFragment

class CustomChannelListActivity : ChannelListActivity() {

    override fun createChannelListFragment(): ChannelListFragment {
        return ChannelListFragment.newInstance {
            showHeader(true)
            showSearch(true)
            headerTitle("Select Channel")
        }
    }
}