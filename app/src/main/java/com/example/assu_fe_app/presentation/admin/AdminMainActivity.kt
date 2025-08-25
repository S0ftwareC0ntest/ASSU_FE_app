package com.example.assu_fe_app.presentation.admin

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.assu_fe_app.R
import com.example.assu_fe_app.databinding.ActivityAdminMainBinding
import com.example.assu_fe_app.presentation.base.BaseActivity
import com.google.firebase.messaging.FirebaseMessaging

class AdminMainActivity : BaseActivity<ActivityAdminMainBinding>(R.layout.activity_admin_main) {

    override fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraPaddingTop = 3 // 8dp 추가
            v.setPadding(
                systemBars.left,
                systemBars.top + extraPaddingTop.dpToPx(v.context),
                systemBars.right,
                0
            )
            insets
        }

        initBottomNavigation()
        handleNavIntent(intent)

        ensureNotificationChannel()
        requestPostNotificationsPermission()
        fetchAndLogFcmToken()
        fetchAndLogFcmToken()
    }

    override fun initObserver() {
        // 옵저버 필요한 경우 작성
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNavIntent(intent)
    }

    private fun handleNavIntent(intent: Intent) {
        val destId = intent.getIntExtra("nav_dest_id", -1)
        if (destId != -1) {
            // BottomNavigationView 에서 해당 메뉴 아이템을 선택하면
            // NavigationUI 가 알아서 navController.navigate(destId) 해 줍니다.
            binding.bottomNavigationView.selectedItemId = destId
        }
    }

    private fun initBottomNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun fetchAndLogFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "토큰 가져오기 실패", task.exception)
                return@addOnCompleteListener
            }
            Log.d("FCM", "FCM 토큰: ${task.result}")
        }
    }

    private fun ensureNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val ch = android.app.NotificationChannel(
                "fcm_default",
                "Default Notifications",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            )
            val nm = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            nm.createNotificationChannel(ch)
        }
    }

    private fun requestPostNotificationsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }
    }
}