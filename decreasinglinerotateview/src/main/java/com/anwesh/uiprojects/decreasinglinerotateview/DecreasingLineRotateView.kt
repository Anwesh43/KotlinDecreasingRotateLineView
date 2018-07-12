package com.anwesh.uiprojects.decreasinglinerotateview

/**
 * Created by anweshmishra on 13/07/18.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas

val NODES : Int = 5

class DecreasingLineRotateView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        return true
    }
}