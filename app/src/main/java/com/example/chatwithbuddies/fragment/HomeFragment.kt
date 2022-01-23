package com.example.chatwithbuddies.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.chatwithbuddies.R
import com.example.chatwithbuddies.activity.ChatActivity
import com.example.chatwithbuddies.activity.CustomChannelListActivity
import com.example.chatwithbuddies.activity.StarredMessagesActivity
import com.example.chatwithbuddies.databinding.FragmentHomeBinding
import com.example.chatwithbuddies.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding

    private var onLogOutListener: OnLogOutListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filters = homeViewModel.initializeChatDomain()
        val viewModelFactory =
            ChannelListViewModelFactory(filters, ChannelListViewModel.DEFAULT_SORT)
        val viewModel: ChannelListViewModel by viewModels { viewModelFactory }

        viewModel.bindView(binding.channelList, this)
        homeViewModel.addChannel()

        binding.channelList.setChannelItemClickListener {
            startActivity(
                ChatActivity.instance(requireContext(), it)
            )
        }

        binding.addChannel.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    requireContext(),
                    CustomChannelListActivity::class.java
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logOut -> {
                homeViewModel.logOutUser()
                onLogOutListener?.onLogOut()
                return true
            }
            R.id.starred -> {
                startActivity(Intent(requireContext(), StarredMessagesActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    interface OnLogOutListener {
        fun onLogOut()
    }

    companion object {
        fun newInstance(onLogOutListener: OnLogOutListener) = HomeFragment().apply {
            this.onLogOutListener = onLogOutListener
        }
    }
}