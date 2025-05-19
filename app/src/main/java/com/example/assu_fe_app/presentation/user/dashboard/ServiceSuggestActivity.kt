package com.example.assu_fe_app.presentation.user.dashboard

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityServiceSuggestBinding
import com.example.assu_fe_app.presentation.base.BaseActivity
import com.example.assu_fe_app.presentation.user.dashboard.adapter.SuggestTargetAdapter

class ServiceSuggestActivity : BaseActivity<ActivityServiceSuggestBinding>(R.layout.activity_service_suggest){

    override fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraPaddingTop = 3
            v.setPadding(
                systemBars.left,
                systemBars.top + extraPaddingTop.dpToPx(v.context),
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        val targetList = resources.getStringArray(R.array.suggest_target).toList()
        val adapter = SuggestTargetAdapter(this, targetList)
        binding.spinnerTarget.adapter = adapter

        binding.spinnerTarget.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                binding.tvServiceSelectTarget.text = selectedItem  // 여기에 선택된 값 출력
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.tvServiceSelectTarget.text="건의대상 선택"
            }
        }

        // 뒤로가기 버튼
        binding.btnSuggestBack.setOnClickListener {
            finish()
        }

        binding.btnSuggestComplete.setOnClickListener {
            val intent = Intent(this, SuggestCompleteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initObserver() {

    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}