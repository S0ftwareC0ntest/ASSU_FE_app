package com.example.assu_fe_app.presentation.partner

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.review.Review
import com.example.assu_fe_app.databinding.FragmentCustomerReviewBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.partner.review.adapter.CustomerReviewAdapter
import com.example.assu_fe_app.presentation.user.review.adapter.ReviewAdapter
import java.time.LocalDateTime

class CustomerReviewFragment : BaseFragment<FragmentCustomerReviewBinding>(R.layout.fragment_customer_review) {

    private lateinit var reviewAdapter: CustomerReviewAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        reviewAdapter = CustomerReviewAdapter()
        reviewAdapter.setData(createDummyData())

        binding.rvCustomerReview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reviewAdapter
        }

        binding.btnCustomerReviewBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun initObserver() {}


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDummyData(): List<Review> {
        return listOf(
            Review(
                marketName = "피자마루",
                studentCategory = "it대학 재학생",
                rate = 4,
                content = "치즈가 정말 풍부하고 맛있었어요!",
                date = LocalDateTime.now().minusDays(2),
                reviewImage = listOf()
            ),
            Review(
                marketName = "치킨나라",
                studentCategory = "it대학 제학생",
                rate = 3,
                content = "무난한 맛이었지만 양은 넉넉했어요.",
                date = LocalDateTime.now().minusDays(4),
                reviewImage = listOf()
            )
        )
    }

}