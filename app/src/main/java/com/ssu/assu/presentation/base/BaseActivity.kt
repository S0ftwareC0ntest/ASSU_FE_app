package com.ssu.assu.presentation.base

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ssu.assu.R


abstract class BaseActivity<V : ViewDataBinding>(@LayoutRes val layoutResource: Int) :
    AppCompatActivity() {

    private var _binding: V? = null
    protected val binding: V get() = _binding!!
    abstract fun initView()
    protected abstract fun initObserver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = ContextCompat.getColor(this, R.color.assu_background)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        initObserver()
        initView()
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val winAttr = window.attributes
        winAttr.flags = if(on) winAttr.flags or bits else winAttr.flags and bits.inv()
        window.attributes = winAttr
    }
}