package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.widget.*

class MainActivity : AppCompatActivity() {

    private lateinit var stopwatch : Chronometer // holds reference to Chronometer
    private var running = false // flag for stopwatch running/stop
    var offset : Long = 0 // will hold the correct time in case of pause, reset.
    lateinit var tvStopwatchNickname : TextView
    //keys for Bundle
    val OFFSET_KEY = "offset"
    val BASE_KEY = "base"
    val RUNNING_KEY = "running"
    val NICKNAME_KEY = "nickname"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // get ref to stopwatch
        stopwatch = findViewById(R.id.stopwatch)
        tvStopwatchNickname = findViewById(R.id.tv_stopwatch_nickname)
        if(savedInstanceState != null) {

            tvStopwatchNickname.text = savedInstanceState.getString(NICKNAME_KEY)
            // the activity is being re-instantiated, and there must be a saved bundle
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if(running) {
                // if it has been running, you want it to run
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else {
                setBaseTime()
            }
        }

        // start button starts the watch if it's not running
        val startButton = findViewById<Button>(R.id.startBTN)
        startButton.setOnClickListener {
            if(!running) {
                setBaseTime() // to set the base property of chronometer
                stopwatch.start() // will start from whatever base-time you set
                running = true
            }
        }

        // pause button will pause the stopwatch
        val pauseButton = findViewById<Button>(R.id.pauseBTN)
        pauseButton.setOnClickListener {
            saveOffset() // whenever interrupted save time as offset
            stopwatch.stop()
            running = false
        }

        // to reset the timer
        val resetButton = findViewById<Button>(R.id.resetBTN)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }

        val stopButton = findViewById<Button>(R.id.stopBTN)
        stopButton.setOnClickListener {
            offset = 0
            running = false
            setBaseTime()
            stopwatch.stop()
        }

        val generateNicknameButton = findViewById<Button>(R.id.bt_generate_nickname)
        generateNicknameButton.setOnClickListener {
            val etNickname = findViewById<EditText>(R.id.et_nickname)
            val etNicknameText = etNickname.text.toString()
            if(etNicknameText.isNotEmpty()) {
                tvStopwatchNickname.text = etNicknameText
            }
        }

    }

    // called right before onDestroy()
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString(NICKNAME_KEY, tvStopwatchNickname.text.toString())
        savedInstanceState.putBoolean(RUNNING_KEY, running)    // a bundle takes input as key-value pair
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        // stopwatch should pause
        offset = SystemClock.elapsedRealtime() - stopwatch.base
        stopwatch.stop()
    }

    override fun onRestart() {
        super.onRestart()
        // stopwatch should start from where it was paused
        stopwatch.base = SystemClock.elapsedRealtime() - offset
        offset = 0
        stopwatch.start()
    }

    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }
}