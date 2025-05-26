package com.example.assu_fe_app.presentation.common.chatting.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.databinding.ItemChattingListBinding
import com.example.assu_fe_app.presentation.common.chatting.ChattingListItem

class ChattingChatListAdapter (
    private val items: List<ChattingListItem>
) : RecyclerView.Adapter<ChattingChatListAdapter.ViewHolder> (){

    inner class ViewHolder(private val binding: ItemChattingListBinding)
        :RecyclerView.ViewHolder(binding.root) {
            fun bind(item: ChattingListItem, isLastItem: Boolean) {
                binding.ivItemChattingListRestaurantProfile.setImageResource(item.profileImage)
                binding.tvChattingCounterpart.text = item.chatName
                binding.tvChattingLastChat.text = item.lastChat

                // 마지막 아이템이면 선 숨기기
                binding.viewLocationSearchResultItemLine.visibility =
                    if (isLastItem) View.GONE else View.VISIBLE
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingChatListAdapter.ViewHolder {
        val binding = ItemChattingListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isLast = position == items.size - 1
        holder.bind(items[position], isLast)
    }

    override fun getItemCount(): Int = items.size
}