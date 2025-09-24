package com.example.assu_fe_app.presentation.admin.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.databinding.ItemAdminPendingContractBinding
import com.example.assu_fe_app.domain.model.partnership.SuspendedPaperModel

class AdminPendingContractAdapter(
    private val onDeleteClick: (SuspendedPaperModel) -> Unit,
    private val onItemClick: (SuspendedPaperModel) -> Unit
) : ListAdapter<SuspendedPaperModel, AdminPendingContractAdapter.ViewHolder>(Diff()) {

    inner class ViewHolder(
        private val binding: ItemAdminPendingContractBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SuspendedPaperModel) {
            binding.tvStoreName.text = item.partnerName
            binding.tvProposalDate.text = item.createdAt.toString().split("T")[0]
            binding.btnDelete.setOnClickListener { onDeleteClick(item) }

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        ViewHolder(ItemAdminPendingContractBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: ViewHolder, pos: Int) = h.bind(getItem(pos))

    private class Diff : DiffUtil.ItemCallback<SuspendedPaperModel>() {
        override fun areItemsTheSame(o: SuspendedPaperModel, n: SuspendedPaperModel) = o.paperId == n.paperId
        override fun areContentsTheSame(o: SuspendedPaperModel, n: SuspendedPaperModel) = o == n
    }
}