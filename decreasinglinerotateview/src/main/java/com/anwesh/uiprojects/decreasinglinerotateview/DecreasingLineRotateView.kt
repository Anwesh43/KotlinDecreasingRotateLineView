package com.anwesh.uiprojects.decreasinglinerotateview

/**
 * Created by anweshmishra on 13/07/18.
 */

import android.app.Activity
import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color

val NODES : Int = 5

class DecreasingLineRotateView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class DLRState(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(stopcb : (Float) -> Unit) {
            scale += 0.1f * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(prevScale)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class DLRAnimator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class DLRNode(var i : Int, val state : DLRState = DLRState()) {

        private var next : DLRNode? = null

        private var prev : DLRNode? = null

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            paint.strokeWidth = Math.min(w, h) / 60
            paint.strokeCap = Paint.Cap.ROUND
            val gap : Float = w / NODES
            val index : Int = (i + 1) % 2
            val xIndex : Int = (i + 1) / 2
            val factor : Int = (1 - 2 * (i % 2))
            canvas.save()
            canvas.translate(w / 4 + xIndex * gap + index * gap, 0.8f * h - gap * xIndex - gap /10)
            canvas.rotate(90f * factor * state.scale)
            canvas.drawLine(0f, 0f, -gap * index, gap * (i % 2), paint)
            canvas.restore()
            next?.draw(canvas, paint)
        }

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < NODES - 1) {
                next = DLRNode(i + 1)
                next?.prev = this
            }
        }

        fun getNext(dir : Int, cb : () -> Unit) : DLRNode {
            var curr : DLRNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class DecreasingRotateLine(var i : Int) {
        private var curr : DLRNode = DLRNode(0)

        private var dir : Int = 1

        fun update(stopcb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                stopcb(it)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            curr.startUpdating(startcb)
        }

        fun draw(canvas : Canvas, paint : Paint) {
            paint.color = Color.parseColor("#303F9F")
            curr.draw(canvas, paint)
        }
    }

    data class Renderer(var view : DecreasingLineRotateView) {

        private val drl : DecreasingRotateLine = DecreasingRotateLine(0)

        private val animator : DLRAnimator = DLRAnimator(view)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            drl.draw(canvas, paint)
            animator.animate {
                drl.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            drl.startUpdating {
                animator.start()
            }
        }
    }

    companion object{

        fun create(activity : Activity) : DecreasingLineRotateView  {
            val view : DecreasingLineRotateView = DecreasingLineRotateView(activity)
            activity.setContentView(view)
            return view
        }
    }
}