package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.ArrayList


//note we now create our own view class that extends the built-in View class
class GameView : View {
    private var game: Game? = null
    private var h: Int = 0
    private var w: Int = 0 //used for storing our height and width of the view
    fun setGame(game: Game?) {
        this.game = game
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onDraw(canvas: Canvas) {
        //Here we get the height and weight
        h = canvas.height
        w = canvas.width
        //update the size for the canvas to the game.
        game?.setSize(h, w)
        Log.d("GAMEVIEW", "h = $h, w = $w")

        //are the coins initiazlied?
        if (!(game!!.coinsInitialized))
            game?.initializeGoldcoins()
        game?.initializeEnemies()


        //Making a new paint object
        val paint = Paint()
        canvas.drawColor(Color.WHITE) //clear entire canvas to white color

        //draw the pacman
        canvas.drawBitmap(game!!.pacBitmap, game?.pacx!!.toFloat(),
                game?.pacy!!.toFloat(), paint)


        for (coin in game!!.coins) {
            if (coin.taken==true){
            canvas.drawBitmap(game!!.coinBitmap, coin.coinx.toFloat(),
                    coin.coiny.toFloat(), paint)
            Log.d("coin",coin.toString())

        }

                canvas.drawBitmap(game!!.enemyBitmap, game?.enemyx!!.toFloat(),
                        game?.enemyy!!.toFloat(), paint)
            }



        game?.doCollisionCheck()
        super.onDraw(canvas)
    }

}
