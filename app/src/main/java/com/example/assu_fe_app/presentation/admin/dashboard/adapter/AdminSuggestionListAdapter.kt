package com.example.assu_fe_app.presentation.admin.dashboard.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_app.databinding.ItemSuggestionListBinding

data class AdminSuggestionItem(
    val departmentInfo: String,
    val status: String,
    val content: String,
    val date: String
)

class AdminSuggestionListAdapter(
    private val items: List<AdminSuggestionItem>
) : RecyclerView.Adapter<AdminSuggestionListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSuggestionListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AdminSuggestionItem) {
            binding.tvDepartment.text = item.departmentInfo
            binding.tvStatus.text = item.status
            binding.tvContent.text = item.content
            binding.tvDate.text = "작성일 | ${item.date}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSuggestionListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}