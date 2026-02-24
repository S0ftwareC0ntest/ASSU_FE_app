package com.ssu.assu.presentation.user.home

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class TransparentHoleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val backgroundPaint = Paint().apply {
        color = 0xAA000000.toInt()  // 반투명 검정
    }

    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private var holeRect = RectF(0f, 0f, 0f, 0f)

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

//    fun updateHoleRectFromView(targetView: View) {
//        val location = IntArray(2)
//        targetView.getLocationOnScreen(location)
//
//        val x = location[0].toFloat()
//        val y = location[1].toFloat()-90
//        val width = targetView.width.toFloat()
//        val height = targetView.height.toFloat()
//
//        holeRect = RectF(x, y, x + width, y + height)
//        invalidate()
//    }


    // 화면 비율 맞추어 홀뷰 조정
    fun updateHoleRectFromView(targetView: View) {
        // 1. 타겟 뷰의 화면상 절대 좌표 구하기
        val targetLocation = IntArray(2)
        targetView.getLocationInWindow(targetLocation)

        // 2. 이 오버레이 뷰 자체의 화면상 절대 좌표 구하기
        val overlayLocation = IntArray(2)
        this.getLocationInWindow(overlayLocation)

        // 3. 타겟 좌표에서 오버레이 좌표를 빼서 '상대적'인 위치 계산
        // 이렇게 하면 상태바 높이나 툴바 위치를 수동으로 계산할 필요가 없습니다.
        val x = (targetLocation[0] - overlayLocation[0]).toFloat()
        val y = (targetLocation[1] - overlayLocation[1]).toFloat()

        val width = targetView.width.toFloat()
        val height = targetView.height.toFloat()

        // 4. 사각형 영역 설정 (여백을 주고 싶다면 여기서 값을 가감하세요)
        holeRect = RectF(x, y, x + width, y + height)

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)
        canvas.drawRoundRect(holeRect, 64f, 64f, clearPaint)
    }
}
