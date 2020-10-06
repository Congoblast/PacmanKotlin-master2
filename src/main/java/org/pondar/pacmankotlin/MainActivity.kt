package org.pondar.pacmankotlin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AlertDialog

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.log
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private var myTimer: Timer = Timer()
    private var countDown: Timer = Timer()
    private var randomNumberCounter: Timer = Timer()
    //you should put in the running in the game class
    private var running = false

    private var soundPool: SoundPool? = null
    private val soundId = 1


    //reference to the game class.
    private var game: Game? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        soundPool!!.load(baseContext, R.raw.music, 1)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        game = Game(this,pointsView)
        textView.text = getString(R.string.timeLeft, game!!.timer)
        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()
        game?.gameOver()

        moveRight.setOnClickListener {
        game?.movePacmanRight(0)
        }
        moveLeft.setOnClickListener {
            game?.movePacmanLeft(0)
        }
        moveUp.setOnClickListener {
            game?.movePacmanUp(0)
        }
        movedown.setOnClickListener {
            game?.movePacmanDown(0)
        }



        pause.setOnClickListener {
            if (running === true)
            {
                pause.setBackgroundColor(Color.RED)
                running = false
            }
            else
            {
                pause.setBackgroundColor(Color.LTGRAY)
                running = true
            }
        }

        running = true //should the game be running?
        //We will call the timer 5 times each second
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()

            }

        }, 0, 200)

        randomNumberCounter.schedule(object :TimerTask(){
            override fun run() {
                timerMethodNumber()
            }

        }, 0, 2000)


        countDown.schedule(object : TimerTask() {
            override fun run() {
                timerMethodCountDown()

            }


        }, 0, 1000)

        }

    private fun timerMethodNumber() {

        this.runOnUiThread(timerTickNumber)


    }

    private val timerTickNumber = Runnable {
        if (running) {
            number()

        }
    }
    private fun timerMethod() {

        this.runOnUiThread(timerTick)


    }
    private fun timerMethodCountDown(){
        this.runOnUiThread(timerTickCountDown)

    }

private fun share(){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Wow look at my craaazy score of ${game?.points} points in just ${((60)-(game?.timer!!))} seconds!!")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
    private val timerTickCountDown = Runnable {
        if (running) {
            game!!.timer--
            timer.text = getString(R.string.timeLeft, game!!.timer)
                if (game!!.points === 8) {
                    pause.setBackgroundColor(Color.RED)
                    running = false

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Damn you're good")
                    builder.setMessage("You just cured the world. How about sharing it?")
                    builder.setPositiveButton("Of course",{ dialogInterface: DialogInterface, i: Int -> share() })

                    builder.setNegativeButton("No",{dialogInterface: DialogInterface, i: Int -> game!!.newGame()})
                    builder.show()
                }
        }
    }

    fun number(){
        game?.randomnumber = (1..4).random()
        Log.d("number:", game!!.randomnumber.toString())
    }


    private val timerTick = Runnable {

        if (running) {
            game!!.counter++

            textView.text = getString(R.string.timerValue,game!!.counter)

            if (game!!.direction==1)
            { // game.moveUp....

                game?.movePacmanLeft (20)
                if (game!!.timer===1)
                {
                    game?.newGame()
                    Log.d("dead",timer.toString())
                    Toast.makeText(this, "You got Corona", Toast.LENGTH_LONG).show()

                }
            }
            else if (game!!.direction==2)
            {
                game?.movePacmanRight(-20)
                if (game!!.timer===1)
                {
                    game?.newGame()
                    Log.d("dead",timer.toString())
                    Toast.makeText(this, "You got Corona", Toast.LENGTH_LONG).show()

                }

            }
            else if (game!!.direction==3)
            {
                game?.movePacmanUp(-20)
                if (game!!.timer===1)
                {
                    game?.newGame()
                    Log.d("dead",timer.toString())
                    Toast.makeText(this, "You got Corona", Toast.LENGTH_LONG).show()

                }
            }
            else if(game!!.direction==4)
            {
                game?.movePacmanDown(20)
                if (game!!.timer===1)
                {
                    game?.newGame()
                    Toast.makeText(this, "You got Corona", Toast.LENGTH_LONG).show()
                }
            }

            if (game!!.randomnumber ===1) {
                game?.moveEnemyRight(-20)
            }
            if (game!!.randomnumber === 2 ){
                game?.moveEnemyLeft(20)
            }
            if(game!!.randomnumber ===3){
                game?.moveEnemyDown(20)
            }
            if(game!!.randomnumber ===4){
                game?.moveEnemyUp(-20)
            }


        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

             if (id==R.id.pause) {
                running = false

        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.newGame()

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun playSound(view: View) {
        soundPool?.play(soundId, 1F, 1F, 0, 1, 1F)
        Toast.makeText(this, "Playing sound. . . .", Toast.LENGTH_SHORT).show()

    }
}

