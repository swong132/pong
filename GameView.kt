package com.example.project5

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.View

class GameView : View {
    val MA : String = "MainActivity"
    private lateinit var paint : Paint
    private lateinit var pong : Pong
    private lateinit var paddleRect : Rect

    constructor (context : Context, width : Int, height : Int  ) : super(context) {
        paint = Paint()
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.strokeWidth = 20.0f
        paint.textSize = 100f

        pong = Pong(context)
        pong.setBallSpeed(width * .0003f)

    }

    fun getGame( ) : Pong {
        return pong
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var cx : Float = pong.getBallCenter().x.toFloat()
        var cy : Float = pong.getBallCenter().y.toFloat()

        canvas.drawCircle(cx, cy, pong.getBallRadius(), paint)
        canvas.drawRect(pong.getPLeft(),pong.getPTop(),pong.getPLeft() + 150f,pong.getPTop() - 50f,paint)

        if (pong.getGameOver()) {
            canvas.drawText("Score: " + pong.getCurrent().toString() , 100f, height/2f, paint)
            canvas.drawText("Best Score: " + pong.getBest().toString() , 100f, height/2f + 125f, paint)
        }
    }

    companion object {
        val DELTA_TIME : Int = 50
    }
}