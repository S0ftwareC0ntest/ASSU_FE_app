package com.ssu.assu.presentation.user.location

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssu.assu.R
import com.ssu.assu.databinding.BottomsheetLocationDetailBinding
import com.ssu.assu.domain.model.location.StoreOnMap
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.bumptech.glide.Glide

class LocationDetailBottomSheet(
    private val item: StoreOnMap,
    private val onReviewClick: (StoreOnMap) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetLocationDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetLocationDetailBinding.inflate(inflater, container, false)

        binding.tvShopName.text = item.name ?: "-"
        
        val benefitCount = item.partnerships?.sumOf { it.benefits.size } ?: 0
        binding.tvGuide.text = "내가 받을 수 있는 제휴 혜택이 ${benefitCount}개 있어요!"
        
        item.profileUrl?.let {
            Glide.with(binding.ivProfile)
                .load(it)
                .into(binding.ivProfile)
        }

        setupBenefitsRecyclerView()

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.RoundCornerBottomSheetDialogTheme)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBenefitsRecyclerView() {
        val benefits = item.partnerships ?: emptyList()

        binding.rvBenefits.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = BenefitAdapter(benefits)
        }
    }

    private inner class BenefitAdapter(private val benefits: List<StoreOnMap.Partnership>) :
        RecyclerView.Adapter<BenefitAdapter.BenefitViewHolder>() {

        inner class BenefitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvAdminName = itemView.findViewById<TextView>(R.id.tv_admin_name)
            private val llItems = itemView.findViewById<LinearLayout>(R.id.ll_benefit_items)
            private val tvTemplate = itemView.findViewById<TextView>(R.id.tv_benefit_template)

            fun bind(partnership: StoreOnMap.Partnership) {
                tvAdminName.text = partnership.adminName
                llItems.removeAllViews()
                
                partnership.benefits.forEach { benefit ->
                    val tv = TextView(itemView.context).apply {
                        text = benefit
                        textSize = 13f
                        setTextColor(itemView.context.getColor(R.color.assu_font_sub))
                        typeface = tvTemplate.typeface
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 0, 0, dpToPx(4))
                        }
                    }
                    llItems.addView(tv)
                }
            }
            
            private fun dpToPx(dp: Int): Int {
                return (dp * itemView.resources.displayMetrics.density).toInt()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenefitViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_benefit, parent, false)
            return BenefitViewHolder(view)
        }

        override fun onBindViewHolder(holder: BenefitViewHolder, position: Int) {
            holder.bind(benefits[position])
        }

        override fun getItemCount() = benefits.size
    }
}
