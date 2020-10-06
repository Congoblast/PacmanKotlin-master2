package org.pondar.pacmankotlin

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import kotlin.math.sqrt


open class Game(private var context: Context,view: TextView) {

    private var pointsView: TextView = view
    var points: Int = 0
    var pacBitmap: Bitmap
    var enemyBitmap: Bitmap
    var coinBitmap: Bitmap
    var randomnumber = 0
    var pacx: Int = 0
    var pacy: Int = 0
    var enemyx:Int = 0
    var enemyy:Int = 0

    var direction: Int = 0
    var counter: Int = 0
    var timer: Int = 0
    override fun toString(): String {
        return points.toString()
    }

    //did we initialize the coins?
    var coinsInitialized = false

    var coins = ArrayList<GoldCoin>()
    var enemies = ArrayList<enemy>()

    //a reference to the gameview
    private var gameView: GameView? = null
    private var h: Int = 0
    private var w: Int = 0 //height and width of screen


//ALl the bitmaps:
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)

    }

    init {
        enemyBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)

    }

    init {
        coinBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pill)
    }


    fun setGameView(view: GameView) {
        this.gameView = view
    }


    fun initializeGoldcoins() {

        //DO Stuff to initialize the array list with coins.
        Log.d("coin", "coins initialized")
        coins.clear()
        coins.add(GoldCoin(true, 0, 200))
        coins.add(GoldCoin(true, 503, 400))
        coins.add(GoldCoin(true, 0, 0))
        coins.add(GoldCoin(true, 800, 0))
        coins.add(GoldCoin(true, 350, 123))
        coins.add(GoldCoin(true, 850, 800))
        coins.add(GoldCoin(true, 0, 800))
        coins.add(GoldCoin(true, 450, 800))
        coinsInitialized = true
    }

    fun initializeEnemies() {
        enemies.add(enemy(700, 600))

    }

    fun gameOver() {
        if (timer === 1) {
            Toast.makeText(context, "You loose", Toast.LENGTH_LONG).show()
            newGame() }
    }


    fun newGame() {

        pacx = 0
        enemyx = 400
        enemyy = 400
        pacy = 400
        timer = 60
        counter = 0
        coinsInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        gameView?.invalidate() //redraw screen

    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

//PACMAN MOVEMENTS
    fun movePacmanRight(pixels: Int) {
        //still within our boundaries?
        if (pacx - pixels > 0) {
            pacx = pacx + pixels
            doCollisionCheck()
            direction = 2
            gameView!!.invalidate()
        }
    }
    fun movePacmanLeft(pixels: Int) {
        //still within our boundaries?
        if (pacx + pixels + pacBitmap.width < w) {
            pacx = pacx + pixels
            doCollisionCheck()
            direction = 1
            gameView!!.invalidate()

        }
    }
    fun movePacmanUp(pixels: Int) {
        //still within our boundaries?
        if (pacy + pixels > 0) {
            pacy = pacy + pixels
            doCollisionCheck()
            direction = 3
            gameView!!.invalidate()
        }
    }
    fun movePacmanDown(pixels: Int) {
        //still within our boundaries?
        if (pacy + pixels + pacBitmap.width < h) {
            pacy = pacy + pixels
            doCollisionCheck()
            direction = 4
            gameView!!.invalidate()
        }
    }


  //GOLDCOIN DISTANCE CALCULATION
    fun distance(pacx: Int, pacy: Int, coinx: Int, coiny: Int): Double {

        var pq = (sqrt(((coinx - pacx) * (coinx - pacx) + (coiny - pacy) * (coiny - pacy)).toDouble()))
        return pq
    }

    //ENEMY DISTANCE CALCULATION
    fun distanceenemy(pacx: Int, pacy: Int, enemyx: Int, enemyy: Int): Double {

        var enemyDist = (sqrt(((enemyx - pacx) * (enemyx - pacx) + (enemyy - pacy) * (enemyy - pacy)).toDouble()))

        return enemyDist
    }
    fun doCollisionCheck() {
        coins.forEach {
            if (distance(pacx, pacy, it.coinx, it.coiny) < 100) {
                if (it.taken === true) {
                    points += 1
                    it.taken = false
                    pointsView.text = "${context.resources.getString(R.string.points)} $points"

                }
            }
            if (distanceenemy(pacx, pacy, enemyx, enemyy) < 100) {
                Toast.makeText(context, "You got Corona", Toast.LENGTH_LONG).show()

                newGame()

            }
        }
    }


    //ENEMY MOVEMENT
    fun moveEnemyRight(pixels: Int) {
        //still within our boundaries?
        if (enemyx - pixels > 0) {

            enemyx = enemyx + pixels
            doCollisionCheck()

            gameView!!.invalidate()
            Log.d("directionz", direction.toString())
        }
    }
        fun moveEnemyLeft(pixels: Int) {
            //still within our boundaries?
            if (enemyx + pixels + enemyBitmap.width < w) {
                enemyx = enemyx + pixels
                doCollisionCheck()

                gameView!!.invalidate()
                Log.d("directionz", direction.toString())


            }

        }
    fun moveEnemyUp(pixels: Int) {
        //still within our boundaries?
        if (enemyy - pixels > 0) {
            enemyy = enemyy + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun moveEnemyDown(pixels: Int) {
        //still within our boundaries?
        if (enemyy + pixels + enemyBitmap.height < h) {
            enemyy = enemyy + pixels
            doCollisionCheck()
            gameView!!.invalidate()

        }
    }
    }




