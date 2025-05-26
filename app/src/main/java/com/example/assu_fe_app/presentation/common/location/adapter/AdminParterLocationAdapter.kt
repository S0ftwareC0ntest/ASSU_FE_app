package com.example.assu_fe_app.presentation.common.location.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.data.dto.location.LocationAdminPartnerSearchResultItem
import com.example.assu_fe_app.databinding.ItemAdminPartnerLocationSearchResultItemBinding
import com.example.assu_fe_app.presentation.user.location.adapter.UserLocationSearchSuccessAdapter.ViewHolder
import com.example.assu_fe_app.presentation.user.review.store.ReviewStoreActivity

class AdminPartnerLocationAdapter(
    private val items: List<LocationAdminPartnerSearchResultItem>
) : RecyclerView.Adapter<AdminPartnerLocationAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemAdminPartnerLocationSearchResultItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocationAdminPartnerSearchResultItem, isLastItem: Boolean) {
            binding.tvItemAdminPartnerLocationSearchResultItemShopName.text = item.shopName

            if (item.isPartnered) {
                binding.tvItemAdminPartnerLocationSearchResultItemPartnered.visibility = View.VISIBLE
                binding.tvItemAdminPartnerLocationSearchResultItemTerm.text = item.term
                binding.tvItemAdminPartnerLocationSearchResultItemContact.text = "제휴 계약서 보기"
            } else {
                binding.tvItemAdminPartnerLocationSearchResultItemPartnered.visibility = View.GONE
                binding.tvItemAdminPartnerLocationSearchResultItemTerm.text = item.address
                binding.tvItemAdminPartnerLocationSearchResultItemContact.text = "문의하기"
            }

            // 마지막 아이템이면 구분선 숨기기
            binding.viewItemAdminPartnerLocationSearchResultItemDivider.visibility =
                if (isLastItem) View.GONE else View.VISIBLE

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, ReviewStoreActivity::class.java)
                intent.putExtra("shopName", item.shopName)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminPartnerLocationSearchResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isLast = position == items.size - 1
        holder.bind(items[position], isLast)
    }

    override fun getItemCount(): Int = items.size
}