package com.example.assu_fe_app.presentation.user.signup

import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.FragmentUserSignUpSchoolBinding
import com.example.assu_fe_app.presentation.base.BaseFragment
import android.widget.ArrayAdapter
import android.view.View
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController

class UserSignUpSchoolFragment : BaseFragment<FragmentUserSignUpSchoolBinding>(R.layout.fragment_user_sign_up_school) {
    override fun initObserver() {
    }

    override fun initView() {
        val schoolList = listOf("학교를 선택해주세요", "숭실대학교")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, schoolList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSchool.adapter = adapter

        binding.spinnerSchool.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = parent.getItemAtPosition(position).toString()
                val drawableId = if (selected == "숭실대학교") R.drawable.btn_basic_selected else R.drawable.btn_basic_unselected
                binding.btnCompleted.setBackgroundResource(drawableId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.btnCompleted.setOnClickListener {
            val selectedSchool = binding.spinnerSchool.selectedItem.toString()
            if (selectedSchool == "숭실대학교") {
                findNavController().navigate(R.id.action_user_school_to_student)
            }
        }
    }

}