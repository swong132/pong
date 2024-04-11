//Andy Jiang and Sharon Wong
package com.example.project5

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.Timer

class MainActivity : AppCompatActivity() {
    val MA : String = "MainActivity"
    private lateinit var gameView: GameView
    private lateinit var pong : Pong
    private lateinit var detector : GestureDetector
    private lateinit var gameTimer : Timer
    private lateinit var  task : GameTimerTask
    private lateinit var view: View
    private var width : Int = 0
    private var start : Boolean = false
    private var gameOver : Boolean = false
    private lateinit var pool : SoundPool
    private var soundPaddleId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        width = resources.displayMetrics.widthPixels
        var height : Int = resources.displayMetrics.heightPixels


        gameView = GameView(this, width, height)
        pong = gameView.getGame()
        setContentView(gameView)

        var th : TouchHandler = TouchHandler()
        detector = GestureDetector( this, th )


        gameTimer = Timer( )
        task = GameTimerTask( this )

        var builder : SoundPool.Builder = SoundPool.Builder( )
        pool = builder.build()
        soundPaddleId = pool.load( this, R.raw.duck_hit, 1 )
    }

    fun playSound( soundId : Int ) {
        pool.play( soundId, 5.0f, 5.0f, 0, 0, 1.0f )
    }

    fun updateBall() {
        pong.startGame()
        if (pong.ballHitsWall()) {
            pong.wallBounce()
        }
        if (pong.ballHitsCeiling()) {
            pong.ceilingBounce()
        }
        if (pong.ballHitsPaddle()) {
            Log.w(MA, pong.getCurrent().toString())
            playSound(soundPaddleId)
            pong.paddleBounce()
        }
        if (pong.ballHitsFloor()) {
            pong.setGameOver(true)
            endGame()
        }
    }

    fun updatePaddle( distanceMoved : Float) {
        var newPLeft : Float = pong.getPLeft() + distanceMoved
        if (newPLeft < 0) {
            newPLeft = 0f
        } else if (newPLeft > width - 150f) {
            newPLeft = width - 150f
        }
        pong.setPLeft(newPLeft)
    }

    fun updateView( ) {
        // Log.w( MA, "Updating view" )
        gameView.postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null)
            detector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun restartGame() {
        pong.setCurrent()
    }

    fun endGame() {
        pong.resetBall()
        if (pong.getCurrent() > pong.getBest()) {
            pong.setBest(pong.getCurrent())
            pong.setPreferences(this)
        }
    }

    inner class TouchHandler : GestureDetector.SimpleOnGestureListener( ) {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            // update cannon angle
            Log.w( MA, "Inside onSingleTapConfirmed" + pong.getBallMoving().toString() )
            if (!pong.getBallMoving()){
                gameTimer.schedule( task, 0L, GameView.DELTA_TIME.toLong())
                pong.setBallMoving(true)
            }
            if (pong.getGameOver() == true) {
                restartGame()
                pong.setGameOver(false)
                pong.setBallSpeed(.2f)
            }

            return super.onSingleTapConfirmed(e)
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            // update the cannon angle
            var distanceMoved = (e2.x - e1!!.x)/10
            updatePaddle(distanceMoved)
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            Log.w( MA, "Inside onDoubleTap" )
            return super.onDoubleTap(e)
        }
    }

}