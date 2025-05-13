package com.example.assu_fe_app

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assu_fe_app.databinding.ActivityMainBinding
import com.example.assu_fe_app.databinding.ActivityMyReviewBinding
import java.time.LocalDateTime

class MyReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyReviewBinding
    private lateinit var reviewAdapter : ReviewAdapter
    val manager = supportFragmentManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMyReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initClick()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAdapter(){
        //adapter초기화
        reviewAdapter = ReviewAdapter()

        binding.rvManageReview.apply {
            layoutManager = LinearLayoutManager(this@MyReviewActivity)
            adapter = reviewAdapter
        }

        // 여기에 review List가 null 일때 ui 업데이트 관련 사항도 해줘야 함.

        reviewAdapter.setData(createDummyData())
        binding.tvManageReviewReviewCount.text = reviewAdapter.itemCount.toString()

    }

    private fun initClick(){
        binding.btnManageReviewBack.setOnClickListener {
            finish()
        }
    }

    // 임의의 DummyData를 생성하는 함수
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDummyData(): List<Review>{
        return listOf(
            Review(
                marketName = "스시천국",
                rate = 5,
                content = "진짜 맛있었어요! 또 가고 싶어요!",
                date = LocalDateTime.now().minusDays(1),
                reviewImage = listOf()
            ),
            Review(
                marketName = "돈까스집",
                rate = 4,
                content = "튀김이 바삭해서 좋았어요. 양도 많아요.",
                date = LocalDateTime.now().minusDays(3),
                reviewImage = listOf()
            )
        )
    }
}