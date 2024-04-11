package com.example.project5

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.graphics.Rect

class Pong {
    private var ballCenter : Point? = Point(550, 35)
    private var ballRadius : Float = 25f
    private var ballSpeed : Float = .2f
    private var best : Int = 0
    private var current : Int = 0
    private var screenWidth : Int = 0
    private var screenHeight : Int = 0
    private var ballAngle : Float = Math.PI.toFloat() / 4
    private var deltaTime : Int = 100
    private var pTop : Float = 0f
    private var pLeft : Float = 0f
    private var ballMoving : Boolean = false
    private var gameOver : Boolean = false



    constructor(context : Context) {
        var pref: SharedPreferences =
            context.getSharedPreferences(
                context.packageName + "_preferences",
                Context.MODE_PRIVATE
            )
        setBest(pref.getInt(PREFERENCE_BEST, 0))
        setCurrent()
        screenWidth = context.resources.displayMetrics.widthPixels
        screenHeight = context.resources.displayMetrics.heightPixels
        pTop = screenHeight.toFloat() - 75f
        pLeft = screenWidth/2f - 125f
        ballMoving = false

    }

    fun setBest(newBest : Int) {
        best = newBest
    }

    fun setCurrent() {
        current = 0
    }

    fun setBallMoving(boolean : Boolean) {
        ballMoving = boolean
    }

    fun setPLeft(l : Float) {
        pLeft = l
    }

    fun setGameOver(b : Boolean) {
        gameOver = b
    }

    fun getBallMoving() : Boolean {
        return ballMoving
    }

    fun getCurrent() : Int {
        return current
    }

    fun getBest() : Int {
        return best
    }

    fun setDeltaTime(newDeltaTime: Int) {
        if (newDeltaTime > 0)
            deltaTime = newDeltaTime
    }

    fun getBallCenter() : Point {
        return ballCenter!!
    }

    fun getBallRadius() : Float {
        return ballRadius
    }

    fun getPTop() : Float {
        return pTop
    }

    fun getPLeft() : Float {
        return pLeft
    }

    fun getGameOver() : Boolean {
        return gameOver
    }

    fun startGame() {
        ballCenter!!.x += (ballSpeed * Math.cos(ballAngle.toDouble()) * deltaTime).toInt()
        ballCenter!!.y += (ballSpeed * Math.sin(ballAngle.toDouble()) * deltaTime).toInt()
    }

    fun setBallSpeed(newSpeed : Float) {
        if (newSpeed > 0)
            ballSpeed = newSpeed
    }

    fun wallBounce() {
        ballAngle = Math.PI.toFloat() - ballAngle
    }

    fun ceilingBounce() {
        ballAngle = -ballAngle
    }

    fun paddleBounce() {
        ballAngle = -ballAngle
    }

    fun ballHitsWall(): Boolean {
        return (ballCenter!!.x + ballRadius >= screenWidth
                || ballCenter!!.x - ballRadius <= 0)
    }

    fun ballHitsCeiling() : Boolean {
        return (ballCenter!!.y - ballRadius <= 0)
    }

    fun ballHitsPaddle() : Boolean {
        if (ballCenter!!.x - ballRadius >= pLeft && ballCenter!!.x + ballRadius <= pLeft + 150f && ballCenter!!.y + (ballRadius * 2) >= pTop) {
            current += 1
            return true
        }
        return false
    }

    fun ballHitsFloor() : Boolean {
        return (ballCenter!!.y + ballRadius >= screenHeight)
    }

    fun resetBall() {
        ballCenter = Point(550, 35)
        ballSpeed = 0f
    }

    fun setPreferences(context : Context) {
        var pref : SharedPreferences =
            context.getSharedPreferences( context.packageName + "_preferences",
                Context.MODE_PRIVATE )
        var edit : SharedPreferences.Editor = pref.edit()

        edit.putInt( PREFERENCE_BEST, best )

        edit.commit()
    }

    companion object {
        private const val PREFERENCE_BEST : String = "best"
    }
}