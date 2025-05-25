package com.example.assu_fe_app.presentation.admin.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.databinding.ItemAssociationListBinding

// 데이터 모델 정의
data class AdminPartnerListItem(
    val partnerName: String,
    val benefitDescription: String,
    val benefitPeriod: String
)

class AdminPartnerListAdapter(
    private val items: List<AdminPartnerListItem>
) : RecyclerView.Adapter<AdminPartnerListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemAssociationListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AdminPartnerListItem) {
            binding.tvAssociationName.text = item.partnerName
            binding.tvBenefitDescription.text = item.benefitDescription
            binding.tvBenefitPeriod.text = item.benefitPeriod
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAssociationListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}