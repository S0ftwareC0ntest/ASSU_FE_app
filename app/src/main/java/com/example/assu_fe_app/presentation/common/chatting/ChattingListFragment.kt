package com.example.assu_fe_app.presentation.common.chatting

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentChattingListBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.common.chatting.adapter.ChattingChatListAdapter

data class ChattingListItem(
    val profileImage: Int,
    val chatName: String,
    val lastChat: String
)

class ChattingListFragment : BaseFragment<FragmentChattingListBinding> (R.layout.fragment_chatting_list){

    private lateinit var adapter: ChattingChatListAdapter

    override fun initObserver() {
        val dummyList = listOf(
            ChattingListItem(R.drawable.ic_restaurant_ex,"인쌩믹주 숭실대점","제휴 가능할까요?"),
            ChattingListItem(R.drawable.ic_restaurant_ex,"인쌩믹주 숭실대점","제휴 가능할까요?"),
            ChattingListItem(R.drawable.ic_restaurant_ex,"인쌩믹주 숭실대점","제휴 가능할까요?"),
            ChattingListItem(R.drawable.ic_restaurant_ex,"인쌩믹주 숭실대점","제휴 가능할까요?"),
            ChattingListItem(R.drawable.ic_restaurant_ex,"인쌩믹주 숭실대점","제휴 가능할까요?"),
            ChattingListItem(R.drawable.ic_restaurant_ex,"인쌩믹주 숭실대점","제휴 가능할까요?")
        )
        adapter = ChattingChatListAdapter(dummyList)
        binding.rvChattingChatList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChattingChatList.adapter = adapter


    }

    override fun initView() {
    }


}