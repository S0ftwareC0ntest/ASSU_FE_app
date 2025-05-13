package com.example.assu_fe_app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.assu_fe_app.databinding.FragmentMypageBinding


class MypageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMypageBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick() // 여기서 호출
    }

    private fun initClick(){
        binding.clAccountComponent1.setOnClickListener {
            val intent = Intent(requireContext(), MyReviewActivity::class.java)
            startActivity(intent)
        }
    }


}