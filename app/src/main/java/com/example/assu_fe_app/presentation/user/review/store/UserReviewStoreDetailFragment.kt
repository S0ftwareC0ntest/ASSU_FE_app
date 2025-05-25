package com.example.assu_fe_app.presentation.user.review.store

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.R
import com.example.assu_fe_app.data.dto.review.Review
import com.example.assu_fe_app.databinding.FragmentReviewStoreDetailBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import com.example.assu_fe_app.presentation.user.review.adapter.UserReviewAdapter
import java.time.LocalDateTime

class UserReviewStoreDetailFragment :
    BaseFragment<FragmentReviewStoreDetailBinding>(R.layout.fragment_review_store_detail) {

    private lateinit var userReviewAdapter: UserReviewAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        userReviewAdapter = UserReviewAdapter(showDeleteButton = false)
        userReviewAdapter.setData(createDummyData())

        binding.fcvReviewStoreRank.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userReviewAdapter
        }

        binding.ivReviewStoreBack.setOnClickListener {
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