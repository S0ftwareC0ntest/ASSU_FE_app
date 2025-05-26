package com.example.assu_fe_app.presentation.common.chatting

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentChattingListBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.chatting.adapter.ChattingChatListAdapter
import com.example.assu_fe_app.data.dto.chatting.ChattingListItem


class ChattingListFragment : BaseFragment<FragmentChattingListBinding> (R.layout.fragment_chatting_list){

    private lateinit var adapter: ChattingChatListAdapter

    override fun initObserver() {
        val dummyList = List(15) {
            ChattingListItem(
                R.drawable.ic_restaurant_ex,
                "인쌩믹주 숭실대점",
                "제휴 가능할까요?"
            )
        }

        adapter = ChattingChatListAdapter(dummyList) { item ->
            val intent = Intent(requireContext(), ChattingActivity::class.java).apply {
                putExtra("CHAT_NAME", item.chatName)
                putExtra("LAST_CHAT", item.lastChat)
                putExtra("CHAT", item.profileImage)
            }
            startActivity(intent)
        }
        binding.rvChattingChatList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChattingChatList.adapter = adapter
    }

    override fun initView() {
    }


}