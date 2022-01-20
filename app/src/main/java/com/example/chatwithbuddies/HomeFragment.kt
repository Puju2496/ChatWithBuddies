package com.example.chatwithbuddies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filters = homeViewModel.initializeChatDomain(requireActivity().applicationContext)
        val viewModelFactory = ChannelListViewModelFactory(filters, ChannelListViewModel.DEFAULT_SORT)
        val viewModel: ChannelListViewModel by viewModels { viewModelFactory }

        viewModel.bindView(binding.channelList, this)
        binding.channelList.setChannelItemClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ChatActivity::class.java
                )
                    .putExtra(ChatActivity.CHANNEL_ID, it.cid))
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}